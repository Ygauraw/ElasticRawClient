package com.silverforge.webconnector.model;

import lombok.Getter;
import lombok.Setter;

public class InvokeBinaryResult extends InvokeResult {
    @Getter
    @Setter
    private byte[] result;
}
