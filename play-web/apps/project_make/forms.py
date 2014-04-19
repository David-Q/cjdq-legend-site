#coding=utf-8
from django import forms
affordLevels = [('2','普通人'),('1','屌丝'),('3','高富帅')]

class ConditionForm(forms.Form):
    lng = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '121.416603','style': 'width:200px;height:40px;font-size:30px'}))
    lat = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '31.218816','style': 'width:200px;height:40px;font-size:30px'}))
    starttime = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '', 'class':'form-control', 'data-date-format':'YYYY/MM/DD|HH:mm', 'style': 'width:400px;height:80px;font-size:30px'}))
    endtime = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '','class':'form-control', 'data-date-format':'YYYY/MM/DD|HH:mm', 'style': 'width:400px;height:80px;font-size:30px'}))
    afford  = forms.ChoiceField(choices=affordLevels, required=False, widget=forms.Select(attrs={'style': 'width:150px;height:80px;font-size:30px'}))
    eatkeyword = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '','style': 'width:150px;height:80px;font-size:30px'}))
    playkeyword = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': '','style': 'width:200px;height:80px;font-size:30px'}))

class RecmdResult():
    rec_title = '?'
    total_price = '?'
    score1 = '?'
    score2 = '?'
    score3 = '?'
    actions = []

class Action():
    actionType = '?'
    shops = []


class Shop():
    distance = '?'
    avg_price = '?'
    pic_url = '/static/img/marker-mini.png'
    shopid = '?'
    shopname = '?'
    star = '?'



