
The is an andoid example of using GoogleApiClient for tracking your location.
The idea is to sample location point each interval time and drawing it. In order to draw one line we need two points. The Naïve way is to draw all the points each time – something that can make "out of memory" exception. In order to overcome this problem I used  concurrentLinkedQueue, in which I manage a data-structure that will cooperate in a way of producer-consumer.
