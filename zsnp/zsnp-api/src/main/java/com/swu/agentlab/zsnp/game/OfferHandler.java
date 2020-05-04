package com.swu.agentlab.zsnp.game;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.message.result.Result;

public interface OfferHandler {

    abstract Offer proposeOffer();

    abstract void receiveOffer(Offer offer);

}
