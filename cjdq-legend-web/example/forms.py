#coding=utf-8
from django import forms


class Form(forms.Form):
    base_url = forms.CharField(max_length=1000, required=False, widget=forms.TextInput(attrs={'class': 'form-control', 'value': 'base'}))
    new_url = forms.CharField(max_length=1000, required=False, widget=forms.TextInput(attrs={'value': 'new'}))
    query = forms.CharField(max_length=100, required=False, widget=forms.TextInput(attrs={'value': 'query'}))
