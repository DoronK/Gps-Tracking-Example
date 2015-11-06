Gps Tracking
==========
This is an android example of using GoogleApiClient for tracking your location.
The idea is to sample location point each interval time and drawing it. In order to draw one line we need two points. The Naïve way is to draw all the points each time – something that can make "out of memory" exception. In order to overcome this problem I used  concurrentLinkedQueue, in which I manage a data-structure that will cooperate in a way of producer-consumer.




License
-----------

    Copyright (C) 2013 DoronK <doronkakuli@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    
    
    

    
    
    
    