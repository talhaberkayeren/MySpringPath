package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
// HTTP GET isteklerini tanımlar.
// Tarayıcıdan bir adrese girildiğinde, istemci ile sunucu arasında bir HTTP protokolü başlar.
// GET, sunucudan veri almak için kullanılan en yaygın HTTP yöntemlerinden biridir

import org.springframework.web.bind.annotation.RestController;
// Bu sınıfın bir REST API denetleyicisi olduğunu belirtir.
// Sınıf içindeki methodlar HTTP isteklerine yanıt verir ve JSON formatında veri döndürür


import org.springframework.web.bind.annotation.RequestParam;
// HTTP isteğindeki query string parametrelerini method parametresiyle eşleştirir.
// Örneğin: http://localhost:8080/greeting?name=Ali
// Bu istekle gelen "Ali" değeri, @RequestParam("name") ile metoda aktarılır

import java.util.concurrent.atomic.AtomicLong;
// counter adında bir sayaç tanımlıyoruz.
// AtomicLong, çoklu kullanıcı aynı anda erişse bile güvenli (thread-safe) bir şekilde çalışır.
// Her /greeting isteğinde bu sayaç 1 artırılır ve bu sayede her kullanıcıya benzersiz bir id atanır.
// Örneğin: İlk kullanıcı için id=1, sonra id=2, sonra 3...
// Neden Atomic? Çünkü uygulamaya çok sayıda istek aynı anda gelebilir ve id çakışması olmamalı.

@RestController
// Bu anotasyon sayesinde bu sınıf bir REST API denetleyicisi (controller) olur.
// Bu sınıf içindeki metodlar, HTTP isteklerine (GET, POST vb.) cevap verebilir hale gelir.
// Normalde bir sınıfı controller yapmak için iki anotasyon gerekir:
//    1) @Controller → sınıfın web katmanında çalıştığını belirtir.
//    2) @ResponseBody → metodların çıktısının direkt olarak HTTP cevabına yazılacağını belirtir.
//
// Ancak @RestController bunların ikisini birlikte içerir. Yani:
// @RestController = @Controller + @ResponseBody
//
// Bu sayede, yazdığımız metodlar bir HTML sayfası döndürmek yerine doğrudan JSON gibi veriler döndürür.
// Örnek: Bir `Greeting` nesnesi dönerse, otomatik olarak JSON formatına çevrilir.

public class GreetingController {
    private static final String template = "Hello, %s!";
    // template adında bir metin şablonu oluşturuyoruz.
    // Bu, her kullanıcıya dönecek selamlaşma mesajının kalıbını belirler.
    // "%s" ifadesi daha sonra gelen bir String ile değiştirilir (örneğin kullanıcının ismi).
    // Örnek: String.format(template, "Talha") çıktısı: "Hello, Talha!"
    // Bu mesaj, istemciye JSON olarak döndürülecek içeriğin bir parçası olacak.

    private final AtomicLong counter = new AtomicLong() ;
    // counter adında bir sayaç tanımlıyoruz.
    // AtomicLong, çoklu kullanıcı aynı anda erişse bile güvenli (thread-safe) bir şekilde çalışır.
    // Her /greeting isteğinde bu sayaç 1 artırılır ve bu sayede her kullanıcıya benzersiz bir id atanır.
    // Örneğin: İlk kullanıcı için id=1, sonra id=2, sonra 3...
    // Neden Atomic? Çünkü uygulamaya çok sayıda istek aynı anda gelebilir ve id çakışması olmamalı

    @GetMapping("/greeting")
    // Bu metod, HTTP GET isteğiyle /greeting adresine gelen çağrıları yakalar.
    // Örneğin kullanıcı tarayıcıya şunu yazarsa: http://localhost:8080/greeting
    // işte bu method çalışır.

    public Greeting greeting(@RequestParam(value = "name", defaultValue = "world") String name) {
        // greeting isimli method tanımlanıyor. Dönüş tipi "Greeting" sınıfıdır.
        // @RequestParam: Kullanıcının gönderdiği URL parametresini okur.
        // value = "name" → URL'de "name" diye bir parametre bekliyoruz (örnek: ?name=Ali)
        // defaultValue = "world" → Eğer kullanıcı ?name= kısmını eklemezse varsayılan olarak "world" değeri kullanılır.
        // Sonuç olarak "name" değişkeni elimizde bir String olur (örnek: "Ali" ya da "world").

        return new Greeting(counter.incrementAndGet(), String.format(template,name));
        // return ifadesi, yeni bir Greeting nesnesi oluşturur ve onu döner.
        // counter.incrementAndGet() → sayaç 1 artırılır (thread-safe). Her çağrıda id değeri farklı olur.
        // String.format(template, name) → template içinde "%s" ifadesi, kullanıcıdan gelen name ile değiştirilir.
        // Örnek: template = "Hello, %s!" ve name = "Ali" → "Hello, Ali!"
        // Yani sonuç: new Greeting(1, "Hello, Ali!") gibi bir nesne döner.
    }

}
