package com.anjani.controller.transaction;

import com.anjani.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class KiranaController {
    @Autowired
    @Lazy
    private StageManager stageManager;
}
