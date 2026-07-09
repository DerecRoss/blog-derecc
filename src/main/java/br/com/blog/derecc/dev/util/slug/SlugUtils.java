package br.com.blog.derecc.dev.util.slug;

import java.text.Normalizer;

public class SlugUtils {

    public static String generate(
            String text
    ) {
        text = Normalizer.normalize(
                text,
                Normalizer.Form.NFD
        );
        return text
                .toLowerCase()
                .replaceAll(
                        "[^a-z0-9\\s]",
                        ""
                )
                .replaceAll(
                        "\\s+",
                        "-"
                );
    }
}
