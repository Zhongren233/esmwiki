package moe.zr.service;


import moe.zr.entry.Card;
import org.bson.types.ObjectId;


public interface CardService {

    Card saveCard(Card card);


    Card queryCard(String id);


}