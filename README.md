Helpers
=======

This is a collection of helper classes for Android.

Features:

* Helpers to "parse" `Boolean` / `Int` / `Long` / `String` / `Date` / `Double` / `BigDecimal` types. This is great for reading from a `Map`, or for example parsing a `Date` field from a `JSONObject`'s property.
* Helpers for handling bitmaps (i.e. EXIF orientation)
* Helpers for Executor objects
* Helpers for loading custom fonts from the assets folder
* Helpers for working with `JSONObject` and `JSONArray`
* Helpers for working with locations
* Helpers for working with Parcels. Mainly encoding/decoding more safely, preserving `null` status of properties.
* Helpers for working with Views (i.e. cause a re-layout *now* instead of in the next ui thread loop)
* `ActivityResultHelper` for simplifying starting activities with result codes (No more spaghetti code in MainActivity...)
* `RuntimePermissionHelper` for simplifying permission requests on Android 6.0 (Marshmallow)
* `SpannableStringBuilderEx` with more help adding spannables
* `CustomTypefaceSpannable` for having a SpannableString with real custom TypeFaces
* Controls that support custom typefaces for (`ButtonEx`, `EditTextEx`, `TextViewEx`)
* A `SwipeRefreshLayout` subclass that makes `setRefreshing(...)` more safe, deferring until after `onMeasure` is called.
* Animations for *resizing* a `View` or a `PopupView`, instead of relative scaling
* More stuff! Just explore tha package...

Have fun and a good luck with your projects!

## Other projects

Checkout [httprequest](https://github.com/danielgindi/android-httprequest) for working with Http request/response easily and without memory limits

## Dependency

[Download from Maven Central (.aar)](https://oss.sonatype.org/index.html#view-repositories;releases~browsestorage~/com/github/danielgindi/helpers/1.1.5/helpers-1.1.5.aar)

**or**

```java
	dependencies {
    	compile 'com.github.danielgindi:helpers:1.1.5'
	}
```

## License

All the code here is under MIT license. Which means you could do virtually anything with the code.
I will appreciate it very much if you keep an attribution where appropriate.

    The MIT License (MIT)
    
    Copyright (c) 2013 Daniel Cohen Gindi (danielgindi@gmail.com)
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
