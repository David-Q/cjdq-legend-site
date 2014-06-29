from django.conf.urls import patterns, include, url
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

urlpatterns = patterns(
    '',
    url(r'^$', 'apps.home.views.home'),
    url(r'mmusic/', 'apps.my_music.views.home'),
    url(r'mblog/', 'apps.my_blog.views.home'),


    url(r'geocode/', 'apps.home.views.getGeoPos'),
    url(r'result/', 'apps.home.views.result'),
    url(r'make/', 'apps.home.views.make'),
)

urlpatterns += staticfiles_urlpatterns()
