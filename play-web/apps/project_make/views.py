#coding=utf-8
import urllib
from django.shortcuts import render_to_response
from django.utils import encoding
import urllib2
import cgi,re
import urlparse
from django.template import RequestContext
import json
import simplejson
from forms import ConditionForm

from django.http import HttpResponse

def home(request):
    lat='31.218816'
    lng='121.416603'
    condition = ConditionForm()
    print ("go!")
    if request.method == 'POST':
        print ("good!")
        form = ConditionForm(request.POST)
        if form.is_valid():
            lng = form.cleaned_data['lng']
            lat = form.cleaned_data['lat']
            starttime = form.cleaned_data['starttime']
            endtime = form.cleaned_data['endtime']
            afford = form.cleaned_data['afford']
            eatkeyword = form.cleaned_data['eatkeyword']
            playkeyword = form.cleaned_data['playkeyword']
        url = createQuery(lng, lat, starttime, endtime, afford, eatkeyword, playkeyword)
        result = urllib2.urlopen(encoding.smart_str(url)).read()
        if result != '':
            print "---------------------"+result
            final_result = simplejson.loads(result)
        print result
        return render_to_response('result.html',{'form': form, 'result':final_result}, context_instance=RequestContext(request))
    
    return render_to_response('condition.html',{'form': condition, 'lat':lat, 'lng':lng}, context_instance=RequestContext(request))
    

def make(request):
    query = request.get_full_path().split("?")

    url = 'http://localhost:1601/make?'+query[1];
    conn = urllib2.urlopen(encoding.smart_str(url))

    data = conn.read()
    if result != '':
            print "---------------------"+data
            final_result = simplejson.loads(data)
    
    return render_to_response('project.html',{'result':final_result}, context_instance=RequestContext(request))
    

def getGeoPos(request):
    query = request.get_full_path().split("?")
    url = 'http://maps.google.com/maps/api/geocode/json?address='+query[1]+'&sensor=false'
    
    proxies = {'http':'http://58.22.0.54:83'} 
    #42.120.41.84:8118
    ProxyHandle = urllib2.ProxyHandler(proxies)
    opener = urllib2.build_opener(ProxyHandle)
    req = urllib2.Request(url)
    conn = opener.open(req)
    '''

    conn = urllib2.urlopen(encoding.smart_str(url))
    '''
    data = conn.read()
    print(data)
    return HttpResponse(data, content_type="application/json;charset=utf-8")


def getSuggestion(request):
    if request.get_full_path() == '/favicon.ico':
        return

    result = ''
    query = request.get_full_path().split("?")
    path = query[0]
    params = {}

    if len(query) > 1:
        print query[1]
        url="https://maps.google.com/maps/suggest?"+query[1]
        print url
        result = urllib2.urlopen(encoding.smart_str(url)).read()
        
        '''m = re.search(r"query:\"[^,]*\"",result)
                                suggest = m.group(0)
                        '''
        m = re.findall(r"query:\"[^,]*\"",result,re.DOTALL)
        print m
        suggestions = ""
        for i in m:
            suggestions += "{\"term\":\""+i[len('query:\"'):len(i)-1]+"\",\"id\":1}";
            suggestions += ","
        suggestions = "{\"term\":\"bla\",\"results\":{\"exercise\":["+suggestions[0:-1]+"]}}"
        

        good='{"term":"bla","results":{"exercise":[{"term":"TRX Hamstring Curl","id":15995},{"term":"TRX Hamstring Curl2","id":15995},{"term":"TRX Hamstring Curl3","id":15995}]}}'
        results = '{"records":[{"suggestion":"A"},{"suggestion":"B"},{"suggestion":"C"}]'
#"{"options\": [\"abc\",\"def\",\"ghj\",\"bmn\"]}"
    return HttpResponse(suggestions, content_type="application/json;charset=utf-8")

def result(request):
    return render_to_response('result.html', context_instance=RequestContext(request))



def createQuery(lng,lat,starttime,endtime,afford, eatkeyword, playkeyword):
    url = 'http://localhost:1601/recmd?glng='+lng+'&glat='+lat+'&starttime='+starttime+'&endtime='+endtime+'&afford='+afford+'&eatkeyword='+eatkeyword+'&playkeyword='+playkeyword
    print(url)
    return url
    '''
    return 'http://localhost:1601/mysql'
    

        if keyword == "":
        keywordquery = ""
    else:
        keywordquery = ",keyword(fullshopname,%s)" % (keyword)

    print keyword
    print encoding.smart_str(keywordquery)
    print keywordquery


    return 'http://192.168.5.149:4053/search/shop?fl=shoppower,gpoi,shopname_e,shopid,phone,address,dist(gpoi,%s:%s)&limit=%s&query=geo(gpoi,%s:%s,5000)%s%s&sort=asc(dist(gpoi,%s:%s)),desc(shoppower)' % (glng,glat,"0,20",glng,glat,shoptypequery,keywordquery,glng,glat)
    '''