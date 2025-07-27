cmake_minimum_required(VERSION 3.18.1)
project("payload")

add_library(
        payload
        SHARED
        payload.c)
