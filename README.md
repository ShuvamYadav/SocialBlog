# SocialBlog

A fully `Reactive` and `Non-blocking` Spring boot application which exposes endpoints for users to sign up and post blogs. Users can also interact with other posts.

## Contents
* [Prerequisites](#prerequisites)
* [General Info](#general-info)
* [Setup](#setup)

## Prerequisites
* Java 8+
* Spring boot 2.3+
* Spring webflux
* MongoDB
* JWT

## General info
Users have to signup and login to get `JWT` token which is used for authorization. Other users can like the posts and see `real time` data streamed whenever someone posts a blog.
Being non-blocking, the application is highly scalable as it does not follow the `thread per request` model. MongoDB is used because of its native Reactive support which is not
yet popular among Relational databases. Tailable cursor is used to keep track of live data being inserted and it is pushed by the database to the server as an event.
There is no API documentation because Swagger is not compatible with Reactive projects yet.

## Setup
Download the file and import it as a Maven or Spring boot project, the endpoints are available at the default `8080` port.
Use a non-blocking client to test the endpoints.
