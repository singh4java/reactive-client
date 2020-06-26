package com.reactive.webclient.reactiveclient.controller;

import com.reactive.webclient.reactiveclient.constants.ItemConstants;
import com.reactive.webclient.reactiveclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {

  WebClient webClient = WebClient.create("http://localhost:8080");

  @GetMapping("/client/retrieve")
  public Flux<Item> getAllItemsUsingRetrieve(){
    return webClient.get()
        .uri(ItemConstants.ITEM_END_POINT_V1)
        .retrieve()
        .bodyToFlux(Item.class)
        .log("Items in client retrieve : ");
  }

  @GetMapping("/client/exchange")
  public Flux<Item> getAllItemsUsingExchange(){
    return webClient.get()
        .uri(ItemConstants.ITEM_END_POINT_V1)
        .exchange()
        .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Item.class))
        .log("Items in client exchange: ");
  }

  @GetMapping("/client/retrieve/{id}")
  public Mono<Item> getAllItemsUsingRetrieveSingleItem(@PathVariable String id){
    return webClient.get()
        .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"),id)
        .retrieve()
        .bodyToMono(Item.class)
        .log("Items in client retrieve single item : ");
  }
  @GetMapping("/client/exchange/{id}")
  public Mono<Item> getAllItemsUsingExchangeSingleItem(@PathVariable String id){
    return webClient.get()
        .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"),id)
        .exchange()
        .flatMap(clientResponse -> clientResponse.bodyToMono(Item.class))
        .log("Items in client exchange single item : ");
  }
  @PostMapping("/client/createitem")
  public Mono<Item> createItem(@RequestBody Item item){
    Mono<Item> mono = Mono.just(item);
    return webClient.post()
        .uri(ItemConstants.ITEM_END_POINT_V1)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mono,Item.class)
        .retrieve()
        .bodyToMono(Item.class)
        .log("created item");
  }

  @PutMapping("/client/updateitem/{id}")
  public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item){
    Mono<Item> mono = Mono.just(item);
    return webClient.put()
        .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"),id)
        .body(mono,Item.class)
        .retrieve()
        .bodyToMono(Item.class)
        .log("updated item");
  }

}
