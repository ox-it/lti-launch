package edu.ksu.lti.launch.test;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LtiSigning {

    static Map<String, List<String>> toQueryParams(String encodedQueryString) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder
            .fromUriString("?" + encodedQueryString)
            .build()
            .getQueryParams();
        return queryParams.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
                .stream()
                .map(s -> UriUtils.decode(s, "UTF-8"))
                .collect(Collectors.toList()))
            );
    }

    static Map<String, String> getRequiredLtiParameters() {
        Map<String, String> additional = new HashMap<>();
        additional.put("lti_version", "LTI-1p0");
        additional.put("resource_link_id", "resourceLinkId");
        additional.put("lti_message_type", "basic-lti-launch-request");
        additional.put("launch_presentation_return_url", "http://tool.consumer/return");
        return additional;
    }
}
