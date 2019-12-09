# LTI Launch

LTI Launch is a project designed to assist in the development of Java based LTI applications that work with the Canvas LMS. It provides functionality to authenticate the OAuth signature of an LTI launch request. After the launch request is verified, the user is forwarded to an initial view specified by the implementing application.

### Build

[![Build Status](https://travis-ci.org/oxctl/lti-launch.svg?branch=master)](https://travis-ci.org/oxctl/lti-launch)

### Technologies Used
- Java 8
- Maven (Compatible with 3.5.2, requires 3.1+)
- Spring 5.1.4
- Spring Security OAuth

### Set Up

The best way to understand how to use this is to look at the sample application https://github.com/oxctl/lti-demo

### Usage

The lookup of tool consumers when handling an LTI launch is done by `ToolConsumerService` and there is simple
implementation in `SingleToolConsumerService`.

### Releasing

This project is deployed to the central repository, once ready to release you can have the release plugin tag everything:

    mvn -Prelease release:clean release:prepare
    
then if that completes successfully a release bundle can be pushed to the staging area of the Sonatype OSS repository with:

    mvn -Prelease release:perform
    
We don't automatically close the staged artifacts so after checking that the files are ok you can login to the [repository](https://oss.sonatype.org/) and release it.

### LTI Variables

The project uses several variables from the LTI launch:

 * `custom_canvas_user_login_id` - If set this will be used as the username on the created principal.
 * `lis_person_sourcedid` - If the canvas specific username isn't found this will be used as the username on the created principal.
 * `context_id` - If set this will be used to say the context for which the principal is valid.
 * `resource_id` - If `context_id` is unset then this will be used to give the principal a context.
 * `roles` - The roles are extracted from this value and set on the returned principal.
 * `custom_canvas_user_isrootaccountadmin` - If `true` then the `ROLE_ROOT_ADMIN` role is added to the principal.
 
This library also allows any project uses this to prevent LTI launches from different domains. Todo this it uses some more LTI variables to detect this:

 * `custom_canvas_api_domain` - If set this value is used to determine where the LTI launch came from.
 * `launch_presentation_return_url` - If `custom_canvas_api_domain` isn't set then this is used to determine where the LTI launch came from.

### Troubleshooting

#### Invalid signature for signature method HMAC-SHA1

If you are having problems with OAuth signatures not matching you should enable debug logging on `edu.ksu.lti.launch.spring.config.LtiAuthenticationFilter` and this will output the string that is being checked and the signature that it should match.

A common problems is that the request is made as HTTPS to a proxy infront of the application and is then passed through as HTTP and this causes a signature mismatch because the the request URL that is checked doesn't matche the request URL that the signature was generated against.

### License
This software is licensed under the LGPL v3 license. Please see the [License.txt file](License.txt) in this repository for license details.

### Multi Node Deployment
Currently nonces are store in memory, this means that in a multi node deployment the same launch can be replayed against multiple nodes as there is no syncing of nonces between them.

### History
This project is currently a fork from the Kansas State University lti-launch codebase and owes it's existence to that project.


