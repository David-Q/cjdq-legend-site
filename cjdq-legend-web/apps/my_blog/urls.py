from django.conf.urls import patterns, url

from jiejing import views

urlpatterns = patterns('',
    url(r'suggest/', 'getSuggestion'),
)

