from django.conf.urls import patterns, url

from example import views

urlpatterns = patterns('',
    url(r'^$', views.diff, name='diff'),
)