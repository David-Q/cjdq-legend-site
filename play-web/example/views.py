#coding=utf-8

from django.shortcuts import render_to_response
from django.template import RequestContext

import urllib2
import simplejson

from forms import Form


def diff(request):
    base_result = ''
    new_result = ''
    if request.method == 'POST':
        form = Form(request.POST)
        if form.is_valid():
            base_url = form.cleaned_data['base_url']
            new_url = form.cleaned_data['new_url']
            query = form.cleaned_data['query']
            if base_url != '' and new_url != '' and query != '':
                base_result = urllib2.urlopen("%s%s" % (base_url.encode('utf8'), query.encode('utf8'))).read()
                if base_result != '':
                    base_result = simplejson.loads(base_result)['records']
                new_result = urllib2.urlopen("%s%s" % (new_url.encode('utf8'), query.encode('utf8'))).read()
                if new_result != '':
                    new_result = simplejson.loads(new_result)['records']
                print base_result
    else:
        form = Form()

    return render_to_response('diff.html', {'form': form, 'base_result': base_result, 'new_result': new_result}, context_instance=RequestContext(request))
