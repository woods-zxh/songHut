from django.urls import path
from . import views

urlpatterns =[
    path('getMelody/', views.getMelody),

]