package solomon.services;

import solomon.Config;
import solomon.addons.Addon;

import java.lang.annotation.Annotation;
import java.util.HashMap;

@FunctionalInterface
public interface Processor {
    Config process(Object command, Context context);

    interface Context {
        Factory factory();
        Config globalConfig();
        AnnotationMap annotationMap();
    }

    class AnnotationMap extends HashMap<Class<? extends Annotation>, Class<? extends Addon>> {
        public boolean containsKey(Annotation annotation) {
            return super.containsKey(annotation.annotationType());
        }

        public Class<? extends Addon> get(Annotation annotation) {
            return super.get(annotation.annotationType());
        }
    }
}
