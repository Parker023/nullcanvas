package com.nullcanvas.blogapp.blog.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Service
public class SlugGeneratorService {

    public String generate(String title) {

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim()))
            throw new IllegalArgumentException("title cannot be empty or null");

        StringBuilder slug = new StringBuilder();
        boolean previousCharIsHyphen = false;

        boolean charsPresent = false;
        title = title.toLowerCase().trim();
        for (char c : title.toCharArray()) {

            if (Character.isLetterOrDigit(c)) {
                slug.append(c);
                previousCharIsHyphen = false;
                charsPresent = true;
            } else if (!previousCharIsHyphen) {
                slug.append("-");
                previousCharIsHyphen = true;
            }

        }

        if (!charsPresent) {
            throw new IllegalArgumentException("title should have at least one character");
        }


        if (!slug.isEmpty() && slug.charAt(0) == '-') {
            slug.deleteCharAt(0);
        }

        if (!slug.isEmpty() && slug.charAt(slug.length() - 1) == '-') {
            slug.deleteCharAt(slug.length() - 1);
        }


        return slug.toString();

    }


}
