package com.hc.plugin;

/**
 * Created by xhrong on 2017/10/12.
 */

class MyExtension {
    def enabled = true

    def setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    def getEnabled() {
        return enabled;
    }
}