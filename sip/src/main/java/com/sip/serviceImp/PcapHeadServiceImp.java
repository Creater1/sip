package com.sip.serviceImp;

import com.sip.service.PcapHeadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PcapHeadServiceImp implements PcapHeadService {

    @Override
    public void setPcapHead(char[] data) {
    }
}
