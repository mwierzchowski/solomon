package solomonx;

import solomonx.api.Addon;
import solomonx.api.Decorator;
import solomonx.api.Observer;

import java.util.List;
import java.util.Map;

public class Config {
    private Config parent;
    private Map<Class<? extends Addon>, Addon> registered;
    private List<? extends Decorator<Object, Object>> decorators;
    private List<? extends Observer<Object, Object>> observers;
}
