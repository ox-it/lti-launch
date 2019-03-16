# LTI Launch

LTI Launch is a project designed to assist in the development of Java based LTI applications that work with the Canvas LMS. It provides functionality to authenticate the OAuth signature of an LTI launch request. After the launch request is verified, the user is forwarded to an initial view specified by the implementing application.

### Build

[![Build Status](https://travis-ci.org/ox-it/lti-launch.svg?branch=master)](https://travis-ci.org/ox-it/lti-launch)

### Technologies Used
- Java 8
- Maven (Compatible with 3.5.2, requires 3.1+)
- Spring 5.1.4
- Spring Security OAuth

### Set Up

The best way to understand how to use this is to look at the sample application https://github.com/ox-it/lti-demo

### Usage

The lookup of tool consumers when handling an LTI launch is done by `ToolConsumerService` and there is simple
implementation in `SingleToolConsumerService`.

### Releasing

This project is deployed to the central repository, once ready to release you can have the release plugin tag everything:

    mvn -Prelease release:clean release:prepare
    
then if that completes successfully a release bundle can be pushed to the staging area of the Sonatype OSS repository with:

    mvn -Prelease release:perform
    
We don't automatically close the staged artifacts so after checking that the files are ok you can login to the [repository](https://oss.sonatype.org/) and release it.

### License
This software is licensed under the LGPL v3 license. Please see the [License.txt file](License.txt) in this repository for license details.

### Multi Node Deployment
Currently nonces are store in memory, this means that in a multi node deployment the same launch can be replayed against multiple nodes as there is no syncing of nonces between them.

### History
This project is currently a fork from the Kansas State University lti-launch codebase and owes it's existence to that project.


