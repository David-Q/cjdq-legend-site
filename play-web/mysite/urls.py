from django.conf.urls import patterns, include, url
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

urlpatterns = patterns(
    '',
    url(r'^$', 'apps.project_make.views.home'),
    url(r'suggest/', 'apps.project_make.views.getSuggestion'),
    url(r'geocode/', 'apps.project_make.views.getGeoPos'),
    url(r'result/', 'apps.project_make.views.result'),
    url(r'make/', 'apps.project_make.views.make'),
)

urlpatterns += staticfiles_urlpatterns()
