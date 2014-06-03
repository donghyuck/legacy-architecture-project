(function() {
	  if (!String.prototype.endsWith) {

	    /*
	     * Determines whether a string ends with the specified suffix.
	     * 
	     * @param  {String} suffix
	     * @return Boolean
	     */
	    String.prototype.endsWith = function(suffix) {
	      return this.indexOf(suffix, this.length - suffix.length) !== -1;
	    };
	  }

	  if (!String.prototype.trim) {

	    /*
	     * Removes whitespace from both sides of a string.
	     * 
	     * @return {String}
	     */
	    String.prototype.trim = function() {
	      return this.replace(/^\s+|\s+$/g, '');
	    };
	  }

	  if (!Array.prototype.indexOf) {

	    /*
	     * The indexOf() method returns the first index at which a given element can be found in the array, or -1 if it is not present.
	     * 
	     * @param  {Variant} searchElement
	     * @param  {Integer} fromIndex
	     * @return {Integer}
	     */
	    Array.prototype.indexOf = function(searchElement, fromIndex) {
	      var i, length, _i;
	      if (this === void 0 || this === null) {
	        throw new TypeError('"this" is null or not defined');
	      }
	      length = this.length >>> 0;
	      fromIndex = +fromIndex || 0;
	      if (Math.abs(fromIndex) === Infinity) {
	        fromIndex = 0;
	      }
	      if (fromIndex < 0) {
	        fromIndex += length;
	        if (fromIndex < 0) {
	          fromIndex = 0;
	        }
	      }
	      for (i = _i = fromIndex; fromIndex <= length ? _i < length : _i > length; i = fromIndex <= length ? ++_i : --_i) {
	        if (this[i] === searchElement) {
	          return i;
	        }
	      }
	      return -1;
	    };
	  }

	  if (!Function.prototype.bind) {
	    Function.prototype.bind = function(oThis) {
	      var aArgs, fBound, fNOP, fToBind;
	      if (typeof this !== "function") {
	        throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
	      }
	      aArgs = Array.prototype.slice.call(arguments, 1);
	      fToBind = this;
	      fNOP = function() {};
	      fBound = function() {
	        return fToBind.apply((this instanceof fNOP && oThis ? this : oThis), aArgs.concat(Array.prototype.slice.call(arguments)));
	      };
	      fNOP.prototype = this.prototype;
	      fBound.prototype = new fNOP();
	      return fBound;
	    };
	  }

	  if (!Object.keys) {
	    Object.keys = (function() {
	      'use strict';
	      var dontEnums, hasDontEnumBug, hasOwnProperty;
	      hasOwnProperty = Object.prototype.hasOwnProperty;
	      hasDontEnumBug = {
	        toString: null
	      }.propertyIsEnumerable('toString') ? false : true;
	      dontEnums = ['toString', 'toLocaleString', 'valueOf', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable', 'constructor'];
	      return function(obj) {
	        var dontEnum, prop, result, _i, _j, _len, _len1;
	        if (typeof obj !== 'object' && (typeof obj !== 'function' || obj === null)) {
	          throw new TypeError('Object.keys called on non-object');
	        }
	        result = [];
	        for (_i = 0, _len = obj.length; _i < _len; _i++) {
	          prop = obj[_i];
	          if (hasOwnProperty.call(obj, prop)) {
	            result.push(prop);
	          }
	        }
	        if (hasDontEnumBug) {
	          for (_j = 0, _len1 = dontEnums.length; _j < _len1; _j++) {
	            dontEnum = dontEnums[_j];
	            if (hasOwnProperty.call(obj, dontEnum)) {
	              result.push(dontEnum);
	            }
	          }
	        }
	        return result;
	      };
	    }).call(this);
	  }


	  /*
	   * Detect screen size.
	   * 
	   * @param  {jQuery Object} $ssw_point
	   * @param  {jQuery Object} $tsw_point
	   * @return {String}
	   */

	  window.getScreenSize = function($ssw_point, $tsw_point) {
	    if ($ssw_point.is(':visible')) {
	      return 'small';
	    } else if ($tsw_point.is(':visible')) {
	      return 'tablet';
	    } else {
	      return 'desktop';
	    }
	  };

	  window.elHasClass = function(el, selector) {
	    return (" " + el.className + " ").indexOf(" " + selector + " ") > -1;
	  };

	  window.elRemoveClass = function(el, selector) {
	    return el.className = (" " + el.className + " ").replace(" " + selector + " ", ' ').trim();
	  };

	}).call(this);
	;
	(function() {
	  var PixelAdminApp, SETTINGS_DEFAULTS;

	  SETTINGS_DEFAULTS = {
	    is_mobile: false,
	    resize_delay: 400,
	    stored_values_prefix: 'pa_',
	    main_menu: {
	      accordion: true,
	      animation_speed: 250,
	      store_state: true,
	      store_state_key: 'mmstate',
	      disable_animation_on: [],
	      dropdown_close_delay: 300,
	      detect_active: true,
	      detect_active_predicate: function(href, url) {
	        return href === url;
	      }
	    },
	    consts: {
	      COLORS: ['#71c73e', '#77b7c5', '#d54848', '#6c42e5', '#e8e64e', '#dd56e6', '#ecad3f', '#618b9d', '#b68b68', '#36a766', '#3156be', '#00b3ff', '#646464', '#a946e8', '#9d9d9d']
	    }
	  };


	  /*
	   * @class PixelAdminApp
	   */

	  PixelAdminApp = function() {
	    this.init = [];
	    this.plugins = {};
	    this.settings = {};
	    this.localStorageSupported = typeof window.Storage !== "undefined" ? true : false;
	    return this;
	  };


	  /*
	   * Start application. Method takes an array of initializers and a settings object(that overrides default settings).
	   * 
	   * @param  {Array} suffix
	   * @param  {Object} settings
	   * @return this
	   */

	  PixelAdminApp.prototype.start = function(init, settings) {
	    if (init == null) {
	      init = [];
	    }
	    if (settings == null) {
	      settings = {};
	    }
	    window.onload = (function(_this) {
	      return function() {
	        var initilizer, _i, _len, _ref;
	        $('html').addClass('pxajs');
	        if (init.length > 0) {
	          $.merge(_this.init, init);
	        }
	        _this.settings = $.extend(true, {}, SETTINGS_DEFAULTS, settings || {});
	        _this.settings.is_mobile = /iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase());
	        if (_this.settings.is_mobile) {
	          if (FastClick) {
	            FastClick.attach(document.body);
	          }
	        }
	        _ref = _this.init;
	        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
	          initilizer = _ref[_i];
	          $.proxy(initilizer, _this)();
	        }
	        $(window).trigger("pa.loaded");
	        return $(window).resize();
	      };
	    })(this);
	    return this;
	  };


	  /*
	   * Add initializer to the stack.
	   * 
	   * @param  {Function} callback
	   */

	  PixelAdminApp.prototype.addInitializer = function(callback) {
	    return this.init.push(callback);
	  };


	  /*
	   * Initialize plugin and add it to the plugins list.
	   * 
	   * @param  {String} plugin_name
	   * @param  {Instance} plugin
	   */

	  PixelAdminApp.prototype.initPlugin = function(plugin_name, plugin) {
	    this.plugins[plugin_name] = plugin;
	    if (plugin.init) {
	      return plugin.init();
	    }
	  };


	  /*
	   * Save value in the localStorage/Cookies.
	   * 
	   * @param  {String}  key
	   * @param  {String}  value
	   * @param  {Boolean} use_cookies
	   */

	  PixelAdminApp.prototype.storeValue = function(key, value, use_cookies) {
	    var e;
	    if (use_cookies == null) {
	      use_cookies = false;
	    }
	    if (this.localStorageSupported && !use_cookies) {
	      try {
	        window.localStorage.setItem(this.settings.stored_values_prefix + key, value);
	        return;
	      } catch (_error) {
	        e = _error;
	        1;
	      }
	    }
	    return document.cookie = this.settings.stored_values_prefix + key + '=' + escape(value);
	  };


	  /*
	   * Save key/value pairs in the localStorage/Cookies.
	   * 
	   * @param  {Object} pairs
	   * @param  {Boolean} use_cookies
	   */

	  PixelAdminApp.prototype.storeValues = function(pairs, use_cookies) {
	    var e, key, value, _results;
	    if (use_cookies == null) {
	      use_cookies = false;
	    }
	    if (this.localStorageSupported && !use_cookies) {
	      try {
	        for (key in pairs) {
	          value = pairs[key];
	          window.localStorage.setItem(this.settings.stored_values_prefix + key, value);
	        }
	        return;
	      } catch (_error) {
	        e = _error;
	        1;
	      }
	    }
	    _results = [];
	    for (key in pairs) {
	      value = pairs[key];
	      _results.push(document.cookie = this.settings.stored_values_prefix + key + '=' + escape(value));
	    }
	    return _results;
	  };


	  /*
	   * Get value from the localStorage/Cookies.
	   * 
	   * @param  {String} key
	   * @param  {Boolean} use_cookies
	   */

	  PixelAdminApp.prototype.getStoredValue = function(key, use_cookies, deflt) {
	    var cookie, cookies, e, k, pos, r, v, _i, _len;
	    if (use_cookies == null) {
	      use_cookies = false;
	    }
	    if (deflt == null) {
	      deflt = null;
	    }
	    if (this.localStorageSupported && !use_cookies) {
	      try {
	        r = window.localStorage.getItem(this.settings.stored_values_prefix + key);
	        return (r ? r : deflt);
	      } catch (_error) {
	        e = _error;
	        1;
	      }
	    }
	    cookies = document.cookie.split(';');
	    for (_i = 0, _len = cookies.length; _i < _len; _i++) {
	      cookie = cookies[_i];
	      pos = cookie.indexOf('=');
	      k = cookie.substr(0, pos).replace(/^\s+|\s+$/g, '');
	      v = cookie.substr(pos + 1).replace(/^\s+|\s+$/g, '');
	      if (k === (this.settings.stored_values_prefix + key)) {
	        return v;
	      }
	    }
	    return deflt;
	  };


	  /*
	   * Get values from the localStorage/Cookies.
	   * 
	   * @param  {Array} keys
	   * @param  {Boolean} use_cookies
	   */

	  PixelAdminApp.prototype.getStoredValues = function(keys, use_cookies, deflt) {
	    var cookie, cookies, e, k, key, pos, r, result, v, _i, _j, _k, _len, _len1, _len2;
	    if (use_cookies == null) {
	      use_cookies = false;
	    }
	    if (deflt == null) {
	      deflt = null;
	    }
	    result = {};
	    for (_i = 0, _len = keys.length; _i < _len; _i++) {
	      key = keys[_i];
	      result[key] = deflt;
	    }
	    if (this.localStorageSupported && !use_cookies) {
	      try {
	        for (_j = 0, _len1 = keys.length; _j < _len1; _j++) {
	          key = keys[_j];
	          r = window.localStorage.getItem(this.settings.stored_values_prefix + key);
	          if (r) {
	            result[key] = r;
	          }
	        }
	        return result;
	      } catch (_error) {
	        e = _error;
	        1;
	      }
	    }
	    cookies = document.cookie.split(';');
	    for (_k = 0, _len2 = cookies.length; _k < _len2; _k++) {
	      cookie = cookies[_k];
	      pos = cookie.indexOf('=');
	      k = cookie.substr(0, pos).replace(/^\s+|\s+$/g, '');
	      v = cookie.substr(pos + 1).replace(/^\s+|\s+$/g, '');
	      if (k === (this.settings.stored_values_prefix + key)) {
	        result[key] = v;
	      }
	    }
	    return result;
	  };

	  PixelAdminApp.Constructor = PixelAdminApp;

	  window.PixelAdmin = new PixelAdminApp;

	}).call(this);
	;
	(function() {
	  var delayedResizeHandler;

	  delayedResizeHandler = function(callback) {
	    var resizeTimer;
	    resizeTimer = null;
	    return function() {
	      if (resizeTimer) {
	        clearTimeout(resizeTimer);
	      }
	      return resizeTimer = setTimeout(function() {
	        resizeTimer = null;
	        return callback.call(this);
	      }, PixelAdmin.settings.resize_delay);
	    };
	  };

	  PixelAdmin.addInitializer(function() {
	    var $ssw_point, $tsw_point, $window, _last_screen;
	    _last_screen = null;
	    $window = $(window);
	    $ssw_point = $('<div id="small-screen-width-point" style="position:absolute;top:-10000px;width:10px;height:10px;background:#fff;"></div>');
	    $tsw_point = $('<div id="tablet-screen-width-point" style="position:absolute;top:-10000px;width:10px;height:10px;background:#fff;"></div>');
	    $('body').append($ssw_point).append($tsw_point);
	    return $window.on('resize', delayedResizeHandler(function() {
	      $window.trigger("pa.resize");
	      if ($ssw_point.is(':visible')) {
	        if (_last_screen !== 'small') {
	          $window.trigger("pa.screen.small");
	        }
	        return _last_screen = 'small';
	      } else if ($tsw_point.is(':visible')) {
	        if (_last_screen !== 'tablet') {
	          $window.trigger("pa.screen.tablet");
	        }
	        return _last_screen = 'tablet';
	      } else {
	        if (_last_screen !== 'desktop') {
	          $window.trigger("pa.screen.desktop");
	        }
	        return _last_screen = 'desktop';
	      }
	    }));
	  });

	}).call(this);
	;

	
	/*
	Copyright 2012 Igor Vaynberg

	Version: 3.4.5 Timestamp: Mon Nov  4 08:22:42 PST 2013

	This software is licensed under the Apache License, Version 2.0 (the "Apache License") or the GNU
	General Public License version 2 (the "GPL License"). You may choose either license to govern your
	use of this software only upon the condition that you accept all of the terms of either the Apache
	License or the GPL License.

	You may obtain a copy of the Apache License and the GPL License at:

	    http://www.apache.org/licenses/LICENSE-2.0
	    http://www.gnu.org/licenses/gpl-2.0.html

	Unless required by applicable law or agreed to in writing, software distributed under the
	Apache License or the GPL Licesnse is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
	CONDITIONS OF ANY KIND, either express or implied. See the Apache License and the GPL License for
	the specific language governing permissions and limitations under the Apache License and the GPL License.
	*/
	(function ($) {
	    if(typeof $.fn.each2 == "undefined") {
	        $.extend($.fn, {
	            /*
	            * 4-10 times faster .each replacement
	            * use it carefully, as it overrides jQuery context of element on each iteration
	            */
	            each2 : function (c) {
	                var j = $([0]), i = -1, l = this.length;
	                while (
	                    ++i < l
	                    && (j.context = j[0] = this[i])
	                    && c.call(j[0], i, j) !== false //"this"=DOM, i=index, j=jQuery object
	                );
	                return this;
	            }
	        });
	    }
	})(jQuery);

	(function ($, undefined) {
	    "use strict";
	    /*global document, window, jQuery, console */

	    if (window.Select2 !== undefined) {
	        return;
	    }

	    var KEY, AbstractSelect2, SingleSelect2, MultiSelect2, nextUid, sizer,
	        lastMousePosition={x:0,y:0}, $document, scrollBarDimensions,

	    KEY = {
	        TAB: 9,
	        ENTER: 13,
	        ESC: 27,
	        SPACE: 32,
	        LEFT: 37,
	        UP: 38,
	        RIGHT: 39,
	        DOWN: 40,
	        SHIFT: 16,
	        CTRL: 17,
	        ALT: 18,
	        PAGE_UP: 33,
	        PAGE_DOWN: 34,
	        HOME: 36,
	        END: 35,
	        BACKSPACE: 8,
	        DELETE: 46,
	        isArrow: function (k) {
	            k = k.which ? k.which : k;
	            switch (k) {
	            case KEY.LEFT:
	            case KEY.RIGHT:
	            case KEY.UP:
	            case KEY.DOWN:
	                return true;
	            }
	            return false;
	        },
	        isControl: function (e) {
	            var k = e.which;
	            switch (k) {
	            case KEY.SHIFT:
	            case KEY.CTRL:
	            case KEY.ALT:
	                return true;
	            }

	            if (e.metaKey) return true;

	            return false;
	        },
	        isFunctionKey: function (k) {
	            k = k.which ? k.which : k;
	            return k >= 112 && k <= 123;
	        }
	    },
	    MEASURE_SCROLLBAR_TEMPLATE = "<div class='select2-measure-scrollbar'></div>",

	    DIACRITICS = {"\u24B6":"A","\uFF21":"A","\u00C0":"A","\u00C1":"A","\u00C2":"A","\u1EA6":"A","\u1EA4":"A","\u1EAA":"A","\u1EA8":"A","\u00C3":"A","\u0100":"A","\u0102":"A","\u1EB0":"A","\u1EAE":"A","\u1EB4":"A","\u1EB2":"A","\u0226":"A","\u01E0":"A","\u00C4":"A","\u01DE":"A","\u1EA2":"A","\u00C5":"A","\u01FA":"A","\u01CD":"A","\u0200":"A","\u0202":"A","\u1EA0":"A","\u1EAC":"A","\u1EB6":"A","\u1E00":"A","\u0104":"A","\u023A":"A","\u2C6F":"A","\uA732":"AA","\u00C6":"AE","\u01FC":"AE","\u01E2":"AE","\uA734":"AO","\uA736":"AU","\uA738":"AV","\uA73A":"AV","\uA73C":"AY","\u24B7":"B","\uFF22":"B","\u1E02":"B","\u1E04":"B","\u1E06":"B","\u0243":"B","\u0182":"B","\u0181":"B","\u24B8":"C","\uFF23":"C","\u0106":"C","\u0108":"C","\u010A":"C","\u010C":"C","\u00C7":"C","\u1E08":"C","\u0187":"C","\u023B":"C","\uA73E":"C","\u24B9":"D","\uFF24":"D","\u1E0A":"D","\u010E":"D","\u1E0C":"D","\u1E10":"D","\u1E12":"D","\u1E0E":"D","\u0110":"D","\u018B":"D","\u018A":"D","\u0189":"D","\uA779":"D","\u01F1":"DZ","\u01C4":"DZ","\u01F2":"Dz","\u01C5":"Dz","\u24BA":"E","\uFF25":"E","\u00C8":"E","\u00C9":"E","\u00CA":"E","\u1EC0":"E","\u1EBE":"E","\u1EC4":"E","\u1EC2":"E","\u1EBC":"E","\u0112":"E","\u1E14":"E","\u1E16":"E","\u0114":"E","\u0116":"E","\u00CB":"E","\u1EBA":"E","\u011A":"E","\u0204":"E","\u0206":"E","\u1EB8":"E","\u1EC6":"E","\u0228":"E","\u1E1C":"E","\u0118":"E","\u1E18":"E","\u1E1A":"E","\u0190":"E","\u018E":"E","\u24BB":"F","\uFF26":"F","\u1E1E":"F","\u0191":"F","\uA77B":"F","\u24BC":"G","\uFF27":"G","\u01F4":"G","\u011C":"G","\u1E20":"G","\u011E":"G","\u0120":"G","\u01E6":"G","\u0122":"G","\u01E4":"G","\u0193":"G","\uA7A0":"G","\uA77D":"G","\uA77E":"G","\u24BD":"H","\uFF28":"H","\u0124":"H","\u1E22":"H","\u1E26":"H","\u021E":"H","\u1E24":"H","\u1E28":"H","\u1E2A":"H","\u0126":"H","\u2C67":"H","\u2C75":"H","\uA78D":"H","\u24BE":"I","\uFF29":"I","\u00CC":"I","\u00CD":"I","\u00CE":"I","\u0128":"I","\u012A":"I","\u012C":"I","\u0130":"I","\u00CF":"I","\u1E2E":"I","\u1EC8":"I","\u01CF":"I","\u0208":"I","\u020A":"I","\u1ECA":"I","\u012E":"I","\u1E2C":"I","\u0197":"I","\u24BF":"J","\uFF2A":"J","\u0134":"J","\u0248":"J","\u24C0":"K","\uFF2B":"K","\u1E30":"K","\u01E8":"K","\u1E32":"K","\u0136":"K","\u1E34":"K","\u0198":"K","\u2C69":"K","\uA740":"K","\uA742":"K","\uA744":"K","\uA7A2":"K","\u24C1":"L","\uFF2C":"L","\u013F":"L","\u0139":"L","\u013D":"L","\u1E36":"L","\u1E38":"L","\u013B":"L","\u1E3C":"L","\u1E3A":"L","\u0141":"L","\u023D":"L","\u2C62":"L","\u2C60":"L","\uA748":"L","\uA746":"L","\uA780":"L","\u01C7":"LJ","\u01C8":"Lj","\u24C2":"M","\uFF2D":"M","\u1E3E":"M","\u1E40":"M","\u1E42":"M","\u2C6E":"M","\u019C":"M","\u24C3":"N","\uFF2E":"N","\u01F8":"N","\u0143":"N","\u00D1":"N","\u1E44":"N","\u0147":"N","\u1E46":"N","\u0145":"N","\u1E4A":"N","\u1E48":"N","\u0220":"N","\u019D":"N","\uA790":"N","\uA7A4":"N","\u01CA":"NJ","\u01CB":"Nj","\u24C4":"O","\uFF2F":"O","\u00D2":"O","\u00D3":"O","\u00D4":"O","\u1ED2":"O","\u1ED0":"O","\u1ED6":"O","\u1ED4":"O","\u00D5":"O","\u1E4C":"O","\u022C":"O","\u1E4E":"O","\u014C":"O","\u1E50":"O","\u1E52":"O","\u014E":"O","\u022E":"O","\u0230":"O","\u00D6":"O","\u022A":"O","\u1ECE":"O","\u0150":"O","\u01D1":"O","\u020C":"O","\u020E":"O","\u01A0":"O","\u1EDC":"O","\u1EDA":"O","\u1EE0":"O","\u1EDE":"O","\u1EE2":"O","\u1ECC":"O","\u1ED8":"O","\u01EA":"O","\u01EC":"O","\u00D8":"O","\u01FE":"O","\u0186":"O","\u019F":"O","\uA74A":"O","\uA74C":"O","\u01A2":"OI","\uA74E":"OO","\u0222":"OU","\u24C5":"P","\uFF30":"P","\u1E54":"P","\u1E56":"P","\u01A4":"P","\u2C63":"P","\uA750":"P","\uA752":"P","\uA754":"P","\u24C6":"Q","\uFF31":"Q","\uA756":"Q","\uA758":"Q","\u024A":"Q","\u24C7":"R","\uFF32":"R","\u0154":"R","\u1E58":"R","\u0158":"R","\u0210":"R","\u0212":"R","\u1E5A":"R","\u1E5C":"R","\u0156":"R","\u1E5E":"R","\u024C":"R","\u2C64":"R","\uA75A":"R","\uA7A6":"R","\uA782":"R","\u24C8":"S","\uFF33":"S","\u1E9E":"S","\u015A":"S","\u1E64":"S","\u015C":"S","\u1E60":"S","\u0160":"S","\u1E66":"S","\u1E62":"S","\u1E68":"S","\u0218":"S","\u015E":"S","\u2C7E":"S","\uA7A8":"S","\uA784":"S","\u24C9":"T","\uFF34":"T","\u1E6A":"T","\u0164":"T","\u1E6C":"T","\u021A":"T","\u0162":"T","\u1E70":"T","\u1E6E":"T","\u0166":"T","\u01AC":"T","\u01AE":"T","\u023E":"T","\uA786":"T","\uA728":"TZ","\u24CA":"U","\uFF35":"U","\u00D9":"U","\u00DA":"U","\u00DB":"U","\u0168":"U","\u1E78":"U","\u016A":"U","\u1E7A":"U","\u016C":"U","\u00DC":"U","\u01DB":"U","\u01D7":"U","\u01D5":"U","\u01D9":"U","\u1EE6":"U","\u016E":"U","\u0170":"U","\u01D3":"U","\u0214":"U","\u0216":"U","\u01AF":"U","\u1EEA":"U","\u1EE8":"U","\u1EEE":"U","\u1EEC":"U","\u1EF0":"U","\u1EE4":"U","\u1E72":"U","\u0172":"U","\u1E76":"U","\u1E74":"U","\u0244":"U","\u24CB":"V","\uFF36":"V","\u1E7C":"V","\u1E7E":"V","\u01B2":"V","\uA75E":"V","\u0245":"V","\uA760":"VY","\u24CC":"W","\uFF37":"W","\u1E80":"W","\u1E82":"W","\u0174":"W","\u1E86":"W","\u1E84":"W","\u1E88":"W","\u2C72":"W","\u24CD":"X","\uFF38":"X","\u1E8A":"X","\u1E8C":"X","\u24CE":"Y","\uFF39":"Y","\u1EF2":"Y","\u00DD":"Y","\u0176":"Y","\u1EF8":"Y","\u0232":"Y","\u1E8E":"Y","\u0178":"Y","\u1EF6":"Y","\u1EF4":"Y","\u01B3":"Y","\u024E":"Y","\u1EFE":"Y","\u24CF":"Z","\uFF3A":"Z","\u0179":"Z","\u1E90":"Z","\u017B":"Z","\u017D":"Z","\u1E92":"Z","\u1E94":"Z","\u01B5":"Z","\u0224":"Z","\u2C7F":"Z","\u2C6B":"Z","\uA762":"Z","\u24D0":"a","\uFF41":"a","\u1E9A":"a","\u00E0":"a","\u00E1":"a","\u00E2":"a","\u1EA7":"a","\u1EA5":"a","\u1EAB":"a","\u1EA9":"a","\u00E3":"a","\u0101":"a","\u0103":"a","\u1EB1":"a","\u1EAF":"a","\u1EB5":"a","\u1EB3":"a","\u0227":"a","\u01E1":"a","\u00E4":"a","\u01DF":"a","\u1EA3":"a","\u00E5":"a","\u01FB":"a","\u01CE":"a","\u0201":"a","\u0203":"a","\u1EA1":"a","\u1EAD":"a","\u1EB7":"a","\u1E01":"a","\u0105":"a","\u2C65":"a","\u0250":"a","\uA733":"aa","\u00E6":"ae","\u01FD":"ae","\u01E3":"ae","\uA735":"ao","\uA737":"au","\uA739":"av","\uA73B":"av","\uA73D":"ay","\u24D1":"b","\uFF42":"b","\u1E03":"b","\u1E05":"b","\u1E07":"b","\u0180":"b","\u0183":"b","\u0253":"b","\u24D2":"c","\uFF43":"c","\u0107":"c","\u0109":"c","\u010B":"c","\u010D":"c","\u00E7":"c","\u1E09":"c","\u0188":"c","\u023C":"c","\uA73F":"c","\u2184":"c","\u24D3":"d","\uFF44":"d","\u1E0B":"d","\u010F":"d","\u1E0D":"d","\u1E11":"d","\u1E13":"d","\u1E0F":"d","\u0111":"d","\u018C":"d","\u0256":"d","\u0257":"d","\uA77A":"d","\u01F3":"dz","\u01C6":"dz","\u24D4":"e","\uFF45":"e","\u00E8":"e","\u00E9":"e","\u00EA":"e","\u1EC1":"e","\u1EBF":"e","\u1EC5":"e","\u1EC3":"e","\u1EBD":"e","\u0113":"e","\u1E15":"e","\u1E17":"e","\u0115":"e","\u0117":"e","\u00EB":"e","\u1EBB":"e","\u011B":"e","\u0205":"e","\u0207":"e","\u1EB9":"e","\u1EC7":"e","\u0229":"e","\u1E1D":"e","\u0119":"e","\u1E19":"e","\u1E1B":"e","\u0247":"e","\u025B":"e","\u01DD":"e","\u24D5":"f","\uFF46":"f","\u1E1F":"f","\u0192":"f","\uA77C":"f","\u24D6":"g","\uFF47":"g","\u01F5":"g","\u011D":"g","\u1E21":"g","\u011F":"g","\u0121":"g","\u01E7":"g","\u0123":"g","\u01E5":"g","\u0260":"g","\uA7A1":"g","\u1D79":"g","\uA77F":"g","\u24D7":"h","\uFF48":"h","\u0125":"h","\u1E23":"h","\u1E27":"h","\u021F":"h","\u1E25":"h","\u1E29":"h","\u1E2B":"h","\u1E96":"h","\u0127":"h","\u2C68":"h","\u2C76":"h","\u0265":"h","\u0195":"hv","\u24D8":"i","\uFF49":"i","\u00EC":"i","\u00ED":"i","\u00EE":"i","\u0129":"i","\u012B":"i","\u012D":"i","\u00EF":"i","\u1E2F":"i","\u1EC9":"i","\u01D0":"i","\u0209":"i","\u020B":"i","\u1ECB":"i","\u012F":"i","\u1E2D":"i","\u0268":"i","\u0131":"i","\u24D9":"j","\uFF4A":"j","\u0135":"j","\u01F0":"j","\u0249":"j","\u24DA":"k","\uFF4B":"k","\u1E31":"k","\u01E9":"k","\u1E33":"k","\u0137":"k","\u1E35":"k","\u0199":"k","\u2C6A":"k","\uA741":"k","\uA743":"k","\uA745":"k","\uA7A3":"k","\u24DB":"l","\uFF4C":"l","\u0140":"l","\u013A":"l","\u013E":"l","\u1E37":"l","\u1E39":"l","\u013C":"l","\u1E3D":"l","\u1E3B":"l","\u017F":"l","\u0142":"l","\u019A":"l","\u026B":"l","\u2C61":"l","\uA749":"l","\uA781":"l","\uA747":"l","\u01C9":"lj","\u24DC":"m","\uFF4D":"m","\u1E3F":"m","\u1E41":"m","\u1E43":"m","\u0271":"m","\u026F":"m","\u24DD":"n","\uFF4E":"n","\u01F9":"n","\u0144":"n","\u00F1":"n","\u1E45":"n","\u0148":"n","\u1E47":"n","\u0146":"n","\u1E4B":"n","\u1E49":"n","\u019E":"n","\u0272":"n","\u0149":"n","\uA791":"n","\uA7A5":"n","\u01CC":"nj","\u24DE":"o","\uFF4F":"o","\u00F2":"o","\u00F3":"o","\u00F4":"o","\u1ED3":"o","\u1ED1":"o","\u1ED7":"o","\u1ED5":"o","\u00F5":"o","\u1E4D":"o","\u022D":"o","\u1E4F":"o","\u014D":"o","\u1E51":"o","\u1E53":"o","\u014F":"o","\u022F":"o","\u0231":"o","\u00F6":"o","\u022B":"o","\u1ECF":"o","\u0151":"o","\u01D2":"o","\u020D":"o","\u020F":"o","\u01A1":"o","\u1EDD":"o","\u1EDB":"o","\u1EE1":"o","\u1EDF":"o","\u1EE3":"o","\u1ECD":"o","\u1ED9":"o","\u01EB":"o","\u01ED":"o","\u00F8":"o","\u01FF":"o","\u0254":"o","\uA74B":"o","\uA74D":"o","\u0275":"o","\u01A3":"oi","\u0223":"ou","\uA74F":"oo","\u24DF":"p","\uFF50":"p","\u1E55":"p","\u1E57":"p","\u01A5":"p","\u1D7D":"p","\uA751":"p","\uA753":"p","\uA755":"p","\u24E0":"q","\uFF51":"q","\u024B":"q","\uA757":"q","\uA759":"q","\u24E1":"r","\uFF52":"r","\u0155":"r","\u1E59":"r","\u0159":"r","\u0211":"r","\u0213":"r","\u1E5B":"r","\u1E5D":"r","\u0157":"r","\u1E5F":"r","\u024D":"r","\u027D":"r","\uA75B":"r","\uA7A7":"r","\uA783":"r","\u24E2":"s","\uFF53":"s","\u00DF":"s","\u015B":"s","\u1E65":"s","\u015D":"s","\u1E61":"s","\u0161":"s","\u1E67":"s","\u1E63":"s","\u1E69":"s","\u0219":"s","\u015F":"s","\u023F":"s","\uA7A9":"s","\uA785":"s","\u1E9B":"s","\u24E3":"t","\uFF54":"t","\u1E6B":"t","\u1E97":"t","\u0165":"t","\u1E6D":"t","\u021B":"t","\u0163":"t","\u1E71":"t","\u1E6F":"t","\u0167":"t","\u01AD":"t","\u0288":"t","\u2C66":"t","\uA787":"t","\uA729":"tz","\u24E4":"u","\uFF55":"u","\u00F9":"u","\u00FA":"u","\u00FB":"u","\u0169":"u","\u1E79":"u","\u016B":"u","\u1E7B":"u","\u016D":"u","\u00FC":"u","\u01DC":"u","\u01D8":"u","\u01D6":"u","\u01DA":"u","\u1EE7":"u","\u016F":"u","\u0171":"u","\u01D4":"u","\u0215":"u","\u0217":"u","\u01B0":"u","\u1EEB":"u","\u1EE9":"u","\u1EEF":"u","\u1EED":"u","\u1EF1":"u","\u1EE5":"u","\u1E73":"u","\u0173":"u","\u1E77":"u","\u1E75":"u","\u0289":"u","\u24E5":"v","\uFF56":"v","\u1E7D":"v","\u1E7F":"v","\u028B":"v","\uA75F":"v","\u028C":"v","\uA761":"vy","\u24E6":"w","\uFF57":"w","\u1E81":"w","\u1E83":"w","\u0175":"w","\u1E87":"w","\u1E85":"w","\u1E98":"w","\u1E89":"w","\u2C73":"w","\u24E7":"x","\uFF58":"x","\u1E8B":"x","\u1E8D":"x","\u24E8":"y","\uFF59":"y","\u1EF3":"y","\u00FD":"y","\u0177":"y","\u1EF9":"y","\u0233":"y","\u1E8F":"y","\u00FF":"y","\u1EF7":"y","\u1E99":"y","\u1EF5":"y","\u01B4":"y","\u024F":"y","\u1EFF":"y","\u24E9":"z","\uFF5A":"z","\u017A":"z","\u1E91":"z","\u017C":"z","\u017E":"z","\u1E93":"z","\u1E95":"z","\u01B6":"z","\u0225":"z","\u0240":"z","\u2C6C":"z","\uA763":"z"};

	    $document = $(document);

	    nextUid=(function() { var counter=1; return function() { return counter++; }; }());


	    function stripDiacritics(str) {
	        var ret, i, l, c;

	        if (!str || str.length < 1) return str;

	        ret = "";
	        for (i = 0, l = str.length; i < l; i++) {
	            c = str.charAt(i);
	            ret += DIACRITICS[c] || c;
	        }
	        return ret;
	    }

	    function indexOf(value, array) {
	        var i = 0, l = array.length;
	        for (; i < l; i = i + 1) {
	            if (equal(value, array[i])) return i;
	        }
	        return -1;
	    }

	    function measureScrollbar () {
	        var $template = $( MEASURE_SCROLLBAR_TEMPLATE );
	        $template.appendTo('body');

	        var dim = {
	            width: $template.width() - $template[0].clientWidth,
	            height: $template.height() - $template[0].clientHeight
	        };
	        $template.remove();

	        return dim;
	    }

	    /**
	     * Compares equality of a and b
	     * @param a
	     * @param b
	     */
	    function equal(a, b) {
	        if (a === b) return true;
	        if (a === undefined || b === undefined) return false;
	        if (a === null || b === null) return false;
	        // Check whether 'a' or 'b' is a string (primitive or object).
	        // The concatenation of an empty string (+'') converts its argument to a string's primitive.
	        if (a.constructor === String) return a+'' === b+''; // a+'' - in case 'a' is a String object
	        if (b.constructor === String) return b+'' === a+''; // b+'' - in case 'b' is a String object
	        return false;
	    }

	    /**
	     * Splits the string into an array of values, trimming each value. An empty array is returned for nulls or empty
	     * strings
	     * @param string
	     * @param separator
	     */
	    function splitVal(string, separator) {
	        var val, i, l;
	        if (string === null || string.length < 1) return [];
	        val = string.split(separator);
	        for (i = 0, l = val.length; i < l; i = i + 1) val[i] = $.trim(val[i]);
	        return val;
	    }

	    function getSideBorderPadding(element) {
	        return element.outerWidth(false) - element.width();
	    }

	    function installKeyUpChangeEvent(element) {
	        var key="keyup-change-value";
	        element.on("keydown", function () {
	            if ($.data(element, key) === undefined) {
	                $.data(element, key, element.val());
	            }
	        });
	        element.on("keyup", function () {
	            var val= $.data(element, key);
	            if (val !== undefined && element.val() !== val) {
	                $.removeData(element, key);
	                element.trigger("keyup-change");
	            }
	        });
	    }

	    $document.on("mousemove", function (e) {
	        lastMousePosition.x = e.pageX;
	        lastMousePosition.y = e.pageY;
	    });

	    /**
	     * filters mouse events so an event is fired only if the mouse moved.
	     *
	     * filters out mouse events that occur when mouse is stationary but
	     * the elements under the pointer are scrolled.
	     */
	    function installFilteredMouseMove(element) {
	        element.on("mousemove", function (e) {
	            var lastpos = lastMousePosition;
	            if (lastpos === undefined || lastpos.x !== e.pageX || lastpos.y !== e.pageY) {
	                $(e.target).trigger("mousemove-filtered", e);
	            }
	        });
	    }

	    /**
	     * Debounces a function. Returns a function that calls the original fn function only if no invocations have been made
	     * within the last quietMillis milliseconds.
	     *
	     * @param quietMillis number of milliseconds to wait before invoking fn
	     * @param fn function to be debounced
	     * @param ctx object to be used as this reference within fn
	     * @return debounced version of fn
	     */
	    function debounce(quietMillis, fn, ctx) {
	        ctx = ctx || undefined;
	        var timeout;
	        return function () {
	            var args = arguments;
	            window.clearTimeout(timeout);
	            timeout = window.setTimeout(function() {
	                fn.apply(ctx, args);
	            }, quietMillis);
	        };
	    }

	    /**
	     * A simple implementation of a thunk
	     * @param formula function used to lazily initialize the thunk
	     * @return {Function}
	     */
	    function thunk(formula) {
	        var evaluated = false,
	            value;
	        return function() {
	            if (evaluated === false) { value = formula(); evaluated = true; }
	            return value;
	        };
	    };

	    function installDebouncedScroll(threshold, element) {
	        var notify = debounce(threshold, function (e) { element.trigger("scroll-debounced", e);});
	        element.on("scroll", function (e) {
	            if (indexOf(e.target, element.get()) >= 0) notify(e);
	        });
	    }

	    function focus($el) {
	        if ($el[0] === document.activeElement) return;

	        /* set the focus in a 0 timeout - that way the focus is set after the processing
	            of the current event has finished - which seems like the only reliable way
	            to set focus */
	        window.setTimeout(function() {
	            var el=$el[0], pos=$el.val().length, range;

	            $el.focus();

	            /* make sure el received focus so we do not error out when trying to manipulate the caret.
	                sometimes modals or others listeners may steal it after its set */
	            if ($el.is(":visible") && el === document.activeElement) {

	                /* after the focus is set move the caret to the end, necessary when we val()
	                    just before setting focus */
	                if(el.setSelectionRange)
	                {
	                    el.setSelectionRange(pos, pos);
	                }
	                else if (el.createTextRange) {
	                    range = el.createTextRange();
	                    range.collapse(false);
	                    range.select();
	                }
	            }
	        }, 0);
	    }

	    function getCursorInfo(el) {
	        el = $(el)[0];
	        var offset = 0;
	        var length = 0;
	        if ('selectionStart' in el) {
	            offset = el.selectionStart;
	            length = el.selectionEnd - offset;
	        } else if ('selection' in document) {
	            el.focus();
	            var sel = document.selection.createRange();
	            length = document.selection.createRange().text.length;
	            sel.moveStart('character', -el.value.length);
	            offset = sel.text.length - length;
	        }
	        return { offset: offset, length: length };
	    }

	    function killEvent(event) {
	        event.preventDefault();
	        event.stopPropagation();
	    }
	    function killEventImmediately(event) {
	        event.preventDefault();
	        event.stopImmediatePropagation();
	    }

	    function measureTextWidth(e) {
	        if (!sizer){
	            var style = e[0].currentStyle || window.getComputedStyle(e[0], null);
	            sizer = $(document.createElement("div")).css({
	                position: "absolute",
	                left: "-10000px",
	                top: "-10000px",
	                display: "none",
	                fontSize: style.fontSize,
	                fontFamily: style.fontFamily,
	                fontStyle: style.fontStyle,
	                fontWeight: style.fontWeight,
	                letterSpacing: style.letterSpacing,
	                textTransform: style.textTransform,
	                whiteSpace: "nowrap"
	            });
	            sizer.attr("class","select2-sizer");
	            $("body").append(sizer);
	        }
	        sizer.text(e.val());
	        return sizer.width();
	    }

	    function syncCssClasses(dest, src, adapter) {
	        var classes, replacements = [], adapted;

	        classes = dest.attr("class");
	        if (classes) {
	            classes = '' + classes; // for IE which returns object
	            $(classes.split(" ")).each2(function() {
	                if (this.indexOf("select2-") === 0) {
	                    replacements.push(this);
	                }
	            });
	        }
	        classes = src.attr("class");
	        if (classes) {
	            classes = '' + classes; // for IE which returns object
	            $(classes.split(" ")).each2(function() {
	                if (this.indexOf("select2-") !== 0) {
	                    adapted = adapter(this);
	                    if (adapted) {
	                        replacements.push(adapted);
	                    }
	                }
	            });
	        }
	        dest.attr("class", replacements.join(" "));
	    }


	    function markMatch(text, term, markup, escapeMarkup) {
	        var match=stripDiacritics(text.toUpperCase()).indexOf(stripDiacritics(term.toUpperCase())),
	            tl=term.length;

	        if (match<0) {
	            markup.push(escapeMarkup(text));
	            return;
	        }

	        markup.push(escapeMarkup(text.substring(0, match)));
	        markup.push("<span class='select2-match'>");
	        markup.push(escapeMarkup(text.substring(match, match + tl)));
	        markup.push("</span>");
	        markup.push(escapeMarkup(text.substring(match + tl, text.length)));
	    }

	    function defaultEscapeMarkup(markup) {
	        var replace_map = {
	            '\\': '&#92;',
	            '&': '&amp;',
	            '<': '&lt;',
	            '>': '&gt;',
	            '"': '&quot;',
	            "'": '&#39;',
	            "/": '&#47;'
	        };

	        return String(markup).replace(/[&<>"'\/\\]/g, function (match) {
	            return replace_map[match];
	        });
	    }

	    /**
	     * Produces an ajax-based query function
	     *
	     * @param options object containing configuration paramters
	     * @param options.params parameter map for the transport ajax call, can contain such options as cache, jsonpCallback, etc. see $.ajax
	     * @param options.transport function that will be used to execute the ajax request. must be compatible with parameters supported by $.ajax
	     * @param options.url url for the data
	     * @param options.data a function(searchTerm, pageNumber, context) that should return an object containing query string parameters for the above url.
	     * @param options.dataType request data type: ajax, jsonp, other datatatypes supported by jQuery's $.ajax function or the transport function if specified
	     * @param options.quietMillis (optional) milliseconds to wait before making the ajaxRequest, helps debounce the ajax function if invoked too often
	     * @param options.results a function(remoteData, pageNumber) that converts data returned form the remote request to the format expected by Select2.
	     *      The expected format is an object containing the following keys:
	     *      results array of objects that will be used as choices
	     *      more (optional) boolean indicating whether there are more results available
	     *      Example: {results:[{id:1, text:'Red'},{id:2, text:'Blue'}], more:true}
	     */
	    function ajax(options) {
	        var timeout, // current scheduled but not yet executed request
	            handler = null,
	            quietMillis = options.quietMillis || 100,
	            ajaxUrl = options.url,
	            self = this;

	        return function (query) {
	            window.clearTimeout(timeout);
	            timeout = window.setTimeout(function () {
	                var data = options.data, // ajax data function
	                    url = ajaxUrl, // ajax url string or function
	                    transport = options.transport || $.fn.select2.ajaxDefaults.transport,
	                    // deprecated - to be removed in 4.0  - use params instead
	                    deprecated = {
	                        type: options.type || 'GET', // set type of request (GET or POST)
	                        cache: options.cache || false,
	                        jsonpCallback: options.jsonpCallback||undefined,
	                        dataType: options.dataType||"json"
	                    },
	                    params = $.extend({}, $.fn.select2.ajaxDefaults.params, deprecated);

	                data = data ? data.call(self, query.term, query.page, query.context) : null;
	                url = (typeof url === 'function') ? url.call(self, query.term, query.page, query.context) : url;

	                if (handler) { handler.abort(); }

	                if (options.params) {
	                    if ($.isFunction(options.params)) {
	                        $.extend(params, options.params.call(self));
	                    } else {
	                        $.extend(params, options.params);
	                    }
	                }

	                $.extend(params, {
	                    url: url,
	                    dataType: options.dataType,
	                    data: data,
	                    success: function (data) {
	                        // TODO - replace query.page with query so users have access to term, page, etc.
	                        var results = options.results(data, query.page);
	                        query.callback(results);
	                    }
	                });
	                handler = transport.call(self, params);
	            }, quietMillis);
	        };
	    }

	    /**
	     * Produces a query function that works with a local array
	     *
	     * @param options object containing configuration parameters. The options parameter can either be an array or an
	     * object.
	     *
	     * If the array form is used it is assumed that it contains objects with 'id' and 'text' keys.
	     *
	     * If the object form is used ti is assumed that it contains 'data' and 'text' keys. The 'data' key should contain
	     * an array of objects that will be used as choices. These objects must contain at least an 'id' key. The 'text'
	     * key can either be a String in which case it is expected that each element in the 'data' array has a key with the
	     * value of 'text' which will be used to match choices. Alternatively, text can be a function(item) that can extract
	     * the text.
	     */
	    function local(options) {
	        var data = options, // data elements
	            dataText,
	            tmp,
	            text = function (item) { return ""+item.text; }; // function used to retrieve the text portion of a data item that is matched against the search

	         if ($.isArray(data)) {
	            tmp = data;
	            data = { results: tmp };
	        }

	         if ($.isFunction(data) === false) {
	            tmp = data;
	            data = function() { return tmp; };
	        }

	        var dataItem = data();
	        if (dataItem.text) {
	            text = dataItem.text;
	            // if text is not a function we assume it to be a key name
	            if (!$.isFunction(text)) {
	                dataText = dataItem.text; // we need to store this in a separate variable because in the next step data gets reset and data.text is no longer available
	                text = function (item) { return item[dataText]; };
	            }
	        }

	        return function (query) {
	            var t = query.term, filtered = { results: [] }, process;
	            if (t === "") {
	                query.callback(data());
	                return;
	            }

	            process = function(datum, collection) {
	                var group, attr;
	                datum = datum[0];
	                if (datum.children) {
	                    group = {};
	                    for (attr in datum) {
	                        if (datum.hasOwnProperty(attr)) group[attr]=datum[attr];
	                    }
	                    group.children=[];
	                    $(datum.children).each2(function(i, childDatum) { process(childDatum, group.children); });
	                    if (group.children.length || query.matcher(t, text(group), datum)) {
	                        collection.push(group);
	                    }
	                } else {
	                    if (query.matcher(t, text(datum), datum)) {
	                        collection.push(datum);
	                    }
	                }
	            };

	            $(data().results).each2(function(i, datum) { process(datum, filtered.results); });
	            query.callback(filtered);
	        };
	    }

	    // TODO javadoc
	    function tags(data) {
	        var isFunc = $.isFunction(data);
	        return function (query) {
	            var t = query.term, filtered = {results: []};
	            $(isFunc ? data() : data).each(function () {
	                var isObject = this.text !== undefined,
	                    text = isObject ? this.text : this;
	                if (t === "" || query.matcher(t, text)) {
	                    filtered.results.push(isObject ? this : {id: this, text: this});
	                }
	            });
	            query.callback(filtered);
	        };
	    }

	    /**
	     * Checks if the formatter function should be used.
	     *
	     * Throws an error if it is not a function. Returns true if it should be used,
	     * false if no formatting should be performed.
	     *
	     * @param formatter
	     */
	    function checkFormatter(formatter, formatterName) {
	        if ($.isFunction(formatter)) return true;
	        if (!formatter) return false;
	        throw new Error(formatterName +" must be a function or a falsy value");
	    }

	    function evaluate(val) {
	        return $.isFunction(val) ? val() : val;
	    }

	    function countResults(results) {
	        var count = 0;
	        $.each(results, function(i, item) {
	            if (item.children) {
	                count += countResults(item.children);
	            } else {
	                count++;
	            }
	        });
	        return count;
	    }

	    /**
	     * Default tokenizer. This function uses breaks the input on substring match of any string from the
	     * opts.tokenSeparators array and uses opts.createSearchChoice to create the choice object. Both of those
	     * two options have to be defined in order for the tokenizer to work.
	     *
	     * @param input text user has typed so far or pasted into the search field
	     * @param selection currently selected choices
	     * @param selectCallback function(choice) callback tho add the choice to selection
	     * @param opts select2's opts
	     * @return undefined/null to leave the current input unchanged, or a string to change the input to the returned value
	     */
	    function defaultTokenizer(input, selection, selectCallback, opts) {
	        var original = input, // store the original so we can compare and know if we need to tell the search to update its text
	            dupe = false, // check for whether a token we extracted represents a duplicate selected choice
	            token, // token
	            index, // position at which the separator was found
	            i, l, // looping variables
	            separator; // the matched separator

	        if (!opts.createSearchChoice || !opts.tokenSeparators || opts.tokenSeparators.length < 1) return undefined;

	        while (true) {
	            index = -1;

	            for (i = 0, l = opts.tokenSeparators.length; i < l; i++) {
	                separator = opts.tokenSeparators[i];
	                index = input.indexOf(separator);
	                if (index >= 0) break;
	            }

	            if (index < 0) break; // did not find any token separator in the input string, bail

	            token = input.substring(0, index);
	            input = input.substring(index + separator.length);

	            if (token.length > 0) {
	                token = opts.createSearchChoice.call(this, token, selection);
	                if (token !== undefined && token !== null && opts.id(token) !== undefined && opts.id(token) !== null) {
	                    dupe = false;
	                    for (i = 0, l = selection.length; i < l; i++) {
	                        if (equal(opts.id(token), opts.id(selection[i]))) {
	                            dupe = true; break;
	                        }
	                    }

	                    if (!dupe) selectCallback(token);
	                }
	            }
	        }

	        if (original!==input) return input;
	    }

	    /**
	     * Creates a new class
	     *
	     * @param superClass
	     * @param methods
	     */
	    function clazz(SuperClass, methods) {
	        var constructor = function () {};
	        constructor.prototype = new SuperClass;
	        constructor.prototype.constructor = constructor;
	        constructor.prototype.parent = SuperClass.prototype;
	        constructor.prototype = $.extend(constructor.prototype, methods);
	        return constructor;
	    }

	    AbstractSelect2 = clazz(Object, {

	        // abstract
	        bind: function (func) {
	            var self = this;
	            return function () {
	                func.apply(self, arguments);
	            };
	        },

	        // abstract
	        init: function (opts) {
	            var results, search, resultsSelector = ".select2-results";

	            // prepare options
	            this.opts = opts = this.prepareOpts(opts);

	            this.id=opts.id;

	            // destroy if called on an existing component
	            if (opts.element.data("select2") !== undefined &&
	                opts.element.data("select2") !== null) {
	                opts.element.data("select2").destroy();
	            }

	            this.container = this.createContainer();

	            this.containerId="s2id_"+(opts.element.attr("id") || "autogen"+nextUid());
	            this.containerSelector="#"+this.containerId.replace(/([;&,\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g, '\\$1');
	            this.container.attr("id", this.containerId);

	            // cache the body so future lookups are cheap
	            this.body = thunk(function() { return opts.element.closest("body"); });

	            syncCssClasses(this.container, this.opts.element, this.opts.adaptContainerCssClass);

	            this.container.attr("style", opts.element.attr("style"));
	            this.container.css(evaluate(opts.containerCss));
	            this.container.addClass(evaluate(opts.containerCssClass));

	            this.elementTabIndex = this.opts.element.attr("tabindex");

	            // swap container for the element
	            this.opts.element
	                .data("select2", this)
	                .attr("tabindex", "-1")
	                .before(this.container)
	                .on("click.select2", killEvent); // do not leak click events

	            this.container.data("select2", this);

	            this.dropdown = this.container.find(".select2-drop");

	            syncCssClasses(this.dropdown, this.opts.element, this.opts.adaptDropdownCssClass);

	            this.dropdown.addClass(evaluate(opts.dropdownCssClass));
	            this.dropdown.data("select2", this);
	            this.dropdown.on("click", killEvent);

	            this.results = results = this.container.find(resultsSelector);
	            this.search = search = this.container.find("input.select2-input");

	            this.queryCount = 0;
	            this.resultsPage = 0;
	            this.context = null;

	            // initialize the container
	            this.initContainer();

	            this.container.on("click", killEvent);

	            installFilteredMouseMove(this.results);
	            this.dropdown.on("mousemove-filtered touchstart touchmove touchend", resultsSelector, this.bind(this.highlightUnderEvent));

	            installDebouncedScroll(80, this.results);
	            this.dropdown.on("scroll-debounced", resultsSelector, this.bind(this.loadMoreIfNeeded));

	            // do not propagate change event from the search field out of the component
	            $(this.container).on("change", ".select2-input", function(e) {e.stopPropagation();});
	            $(this.dropdown).on("change", ".select2-input", function(e) {e.stopPropagation();});

	            // if jquery.mousewheel plugin is installed we can prevent out-of-bounds scrolling of results via mousewheel
	            if ($.fn.mousewheel) {
	                results.mousewheel(function (e, delta, deltaX, deltaY) {
	                    var top = results.scrollTop();
	                    if (deltaY > 0 && top - deltaY <= 0) {
	                        results.scrollTop(0);
	                        killEvent(e);
	                    } else if (deltaY < 0 && results.get(0).scrollHeight - results.scrollTop() + deltaY <= results.height()) {
	                        results.scrollTop(results.get(0).scrollHeight - results.height());
	                        killEvent(e);
	                    }
	                });
	            }

	            installKeyUpChangeEvent(search);
	            search.on("keyup-change input paste", this.bind(this.updateResults));
	            search.on("focus", function () { search.addClass("select2-focused"); });
	            search.on("blur", function () { search.removeClass("select2-focused");});

	            this.dropdown.on("mouseup", resultsSelector, this.bind(function (e) {
	                if ($(e.target).closest(".select2-result-selectable").length > 0) {
	                    this.highlightUnderEvent(e);
	                    this.selectHighlighted(e);
	                }
	            }));

	            // trap all mouse events from leaving the dropdown. sometimes there may be a modal that is listening
	            // for mouse events outside of itself so it can close itself. since the dropdown is now outside the select2's
	            // dom it will trigger the popup close, which is not what we want
	            this.dropdown.on("click mouseup mousedown", function (e) { e.stopPropagation(); });

	            if ($.isFunction(this.opts.initSelection)) {
	                // initialize selection based on the current value of the source element
	                this.initSelection();

	                // if the user has provided a function that can set selection based on the value of the source element
	                // we monitor the change event on the element and trigger it, allowing for two way synchronization
	                this.monitorSource();
	            }

	            if (opts.maximumInputLength !== null) {
	                this.search.attr("maxlength", opts.maximumInputLength);
	            }

	            var disabled = opts.element.prop("disabled");
	            if (disabled === undefined) disabled = false;
	            this.enable(!disabled);

	            var readonly = opts.element.prop("readonly");
	            if (readonly === undefined) readonly = false;
	            this.readonly(readonly);

	            // Calculate size of scrollbar
	            scrollBarDimensions = scrollBarDimensions || measureScrollbar();

	            this.autofocus = opts.element.prop("autofocus");
	            opts.element.prop("autofocus", false);
	            if (this.autofocus) this.focus();

	            this.nextSearchTerm = undefined;
	        },

	        // abstract
	        destroy: function () {
	            var element=this.opts.element, select2 = element.data("select2");

	            this.close();

	            if (this.propertyObserver) { delete this.propertyObserver; this.propertyObserver = null; }

	            if (select2 !== undefined) {
	                select2.container.remove();
	                select2.dropdown.remove();
	                element
	                    .removeClass("select2-offscreen")
	                    .removeData("select2")
	                    .off(".select2")
	                    .prop("autofocus", this.autofocus || false);
	                if (this.elementTabIndex) {
	                    element.attr({tabindex: this.elementTabIndex});
	                } else {
	                    element.removeAttr("tabindex");
	                }
	                element.show();
	            }
	        },

	        // abstract
	        optionToData: function(element) {
	            if (element.is("option")) {
	                return {
	                    id:element.prop("value"),
	                    text:element.text(),
	                    element: element.get(),
	                    css: element.attr("class"),
	                    disabled: element.prop("disabled"),
	                    locked: equal(element.attr("locked"), "locked") || equal(element.data("locked"), true)
	                };
	            } else if (element.is("optgroup")) {
	                return {
	                    text:element.attr("label"),
	                    children:[],
	                    element: element.get(),
	                    css: element.attr("class")
	                };
	            }
	        },

	        // abstract
	        prepareOpts: function (opts) {
	            var element, select, idKey, ajaxUrl, self = this;

	            element = opts.element;

	            if (element.get(0).tagName.toLowerCase() === "select") {
	                this.select = select = opts.element;
	            }

	            if (select) {
	                // these options are not allowed when attached to a select because they are picked up off the element itself
	                $.each(["id", "multiple", "ajax", "query", "createSearchChoice", "initSelection", "data", "tags"], function () {
	                    if (this in opts) {
	                        throw new Error("Option '" + this + "' is not allowed for Select2 when attached to a <select> element.");
	                    }
	                });
	            }

	            opts = $.extend({}, {
	                populateResults: function(container, results, query) {
	                    var populate, id=this.opts.id;

	                    populate=function(results, container, depth) {

	                        var i, l, result, selectable, disabled, compound, node, label, innerContainer, formatted;

	                        results = opts.sortResults(results, container, query);

	                        for (i = 0, l = results.length; i < l; i = i + 1) {

	                            result=results[i];

	                            disabled = (result.disabled === true);
	                            selectable = (!disabled) && (id(result) !== undefined);

	                            compound=result.children && result.children.length > 0;

	                            node=$("<li></li>");
	                            node.addClass("select2-results-dept-"+depth);
	                            node.addClass("select2-result");
	                            node.addClass(selectable ? "select2-result-selectable" : "select2-result-unselectable");
	                            if (disabled) { node.addClass("select2-disabled"); }
	                            if (compound) { node.addClass("select2-result-with-children"); }
	                            node.addClass(self.opts.formatResultCssClass(result));

	                            label=$(document.createElement("div"));
	                            label.addClass("select2-result-label");

	                            formatted=opts.formatResult(result, label, query, self.opts.escapeMarkup);
	                            if (formatted!==undefined) {
	                                label.html(formatted);
	                            }

	                            node.append(label);

	                            if (compound) {

	                                innerContainer=$("<ul></ul>");
	                                innerContainer.addClass("select2-result-sub");
	                                populate(result.children, innerContainer, depth+1);
	                                node.append(innerContainer);
	                            }

	                            node.data("select2-data", result);
	                            container.append(node);
	                        }
	                    };

	                    populate(results, container, 0);
	                }
	            }, $.fn.select2.defaults, opts);

	            if (typeof(opts.id) !== "function") {
	                idKey = opts.id;
	                opts.id = function (e) { return e[idKey]; };
	            }

	            if ($.isArray(opts.element.data("select2Tags"))) {
	                if ("tags" in opts) {
	                    throw "tags specified as both an attribute 'data-select2-tags' and in options of Select2 " + opts.element.attr("id");
	                }
	                opts.tags=opts.element.data("select2Tags");
	            }

	            if (select) {
	                opts.query = this.bind(function (query) {
	                    var data = { results: [], more: false },
	                        term = query.term,
	                        children, placeholderOption, process;

	                    process=function(element, collection) {
	                        var group;
	                        if (element.is("option")) {
	                            if (query.matcher(term, element.text(), element)) {
	                                collection.push(self.optionToData(element));
	                            }
	                        } else if (element.is("optgroup")) {
	                            group=self.optionToData(element);
	                            element.children().each2(function(i, elm) { process(elm, group.children); });
	                            if (group.children.length>0) {
	                                collection.push(group);
	                            }
	                        }
	                    };

	                    children=element.children();

	                    // ignore the placeholder option if there is one
	                    if (this.getPlaceholder() !== undefined && children.length > 0) {
	                        placeholderOption = this.getPlaceholderOption();
	                        if (placeholderOption) {
	                            children=children.not(placeholderOption);
	                        }
	                    }

	                    children.each2(function(i, elm) { process(elm, data.results); });

	                    query.callback(data);
	                });
	                // this is needed because inside val() we construct choices from options and there id is hardcoded
	                opts.id=function(e) { return e.id; };
	                opts.formatResultCssClass = function(data) { return data.css; };
	            } else {
	                if (!("query" in opts)) {

	                    if ("ajax" in opts) {
	                        ajaxUrl = opts.element.data("ajax-url");
	                        if (ajaxUrl && ajaxUrl.length > 0) {
	                            opts.ajax.url = ajaxUrl;
	                        }
	                        opts.query = ajax.call(opts.element, opts.ajax);
	                    } else if ("data" in opts) {
	                        opts.query = local(opts.data);
	                    } else if ("tags" in opts) {
	                        opts.query = tags(opts.tags);
	                        if (opts.createSearchChoice === undefined) {
	                            opts.createSearchChoice = function (term) { return {id: $.trim(term), text: $.trim(term)}; };
	                        }
	                        if (opts.initSelection === undefined) {
	                            opts.initSelection = function (element, callback) {
	                                var data = [];
	                                $(splitVal(element.val(), opts.separator)).each(function () {
	                                    var obj = { id: this, text: this },
	                                        tags = opts.tags;
	                                    if ($.isFunction(tags)) tags=tags();
	                                    $(tags).each(function() { if (equal(this.id, obj.id)) { obj = this; return false; } });
	                                    data.push(obj);
	                                });

	                                callback(data);
	                            };
	                        }
	                    }
	                }
	            }
	            if (typeof(opts.query) !== "function") {
	                throw "query function not defined for Select2 " + opts.element.attr("id");
	            }

	            return opts;
	        },

	        /**
	         * Monitor the original element for changes and update select2 accordingly
	         */
	        // abstract
	        monitorSource: function () {
	            var el = this.opts.element, sync, observer;

	            el.on("change.select2", this.bind(function (e) {
	                if (this.opts.element.data("select2-change-triggered") !== true) {
	                    this.initSelection();
	                }
	            }));

	            sync = this.bind(function () {

	                // sync enabled state
	                var disabled = el.prop("disabled");
	                if (disabled === undefined) disabled = false;
	                this.enable(!disabled);

	                var readonly = el.prop("readonly");
	                if (readonly === undefined) readonly = false;
	                this.readonly(readonly);

	                syncCssClasses(this.container, this.opts.element, this.opts.adaptContainerCssClass);
	                this.container.addClass(evaluate(this.opts.containerCssClass));

	                syncCssClasses(this.dropdown, this.opts.element, this.opts.adaptDropdownCssClass);
	                this.dropdown.addClass(evaluate(this.opts.dropdownCssClass));

	            });

	            // IE8-10
	            el.on("propertychange.select2", sync);

	            // hold onto a reference of the callback to work around a chromium bug
	            if (this.mutationCallback === undefined) {
	                this.mutationCallback = function (mutations) {
	                    mutations.forEach(sync);
	                }
	            }

	            // safari, chrome, firefox, IE11
	            observer = window.MutationObserver || window.WebKitMutationObserver|| window.MozMutationObserver;
	            if (observer !== undefined) {
	                if (this.propertyObserver) { delete this.propertyObserver; this.propertyObserver = null; }
	                this.propertyObserver = new observer(this.mutationCallback);
	                this.propertyObserver.observe(el.get(0), { attributes:true, subtree:false });
	            }
	        },

	        // abstract
	        triggerSelect: function(data) {
	            var evt = $.Event("select2-selecting", { val: this.id(data), object: data });
	            this.opts.element.trigger(evt);
	            return !evt.isDefaultPrevented();
	        },

	        /**
	         * Triggers the change event on the source element
	         */
	        // abstract
	        triggerChange: function (details) {

	            details = details || {};
	            details= $.extend({}, details, { type: "change", val: this.val() });
	            // prevents recursive triggering
	            this.opts.element.data("select2-change-triggered", true);
	            this.opts.element.trigger(details);
	            this.opts.element.data("select2-change-triggered", false);

	            // some validation frameworks ignore the change event and listen instead to keyup, click for selects
	            // so here we trigger the click event manually
	            this.opts.element.click();

	            // ValidationEngine ignorea the change event and listens instead to blur
	            // so here we trigger the blur event manually if so desired
	            if (this.opts.blurOnChange)
	                this.opts.element.blur();
	        },

	        //abstract
	        isInterfaceEnabled: function()
	        {
	            return this.enabledInterface === true;
	        },

	        // abstract
	        enableInterface: function() {
	            var enabled = this._enabled && !this._readonly,
	                disabled = !enabled;

	            if (enabled === this.enabledInterface) return false;

	            this.container.toggleClass("select2-container-disabled", disabled);
	            this.close();
	            this.enabledInterface = enabled;

	            return true;
	        },

	        // abstract
	        enable: function(enabled) {
	            if (enabled === undefined) enabled = true;
	            if (this._enabled === enabled) return;
	            this._enabled = enabled;

	            this.opts.element.prop("disabled", !enabled);
	            this.enableInterface();
	        },

	        // abstract
	        disable: function() {
	            this.enable(false);
	        },

	        // abstract
	        readonly: function(enabled) {
	            if (enabled === undefined) enabled = false;
	            if (this._readonly === enabled) return false;
	            this._readonly = enabled;

	            this.opts.element.prop("readonly", enabled);
	            this.enableInterface();
	            return true;
	        },

	        // abstract
	        opened: function () {
	            return this.container.hasClass("select2-dropdown-open");
	        },

	        // abstract
	        positionDropdown: function() {
	            var $dropdown = this.dropdown,
	                offset = this.container.offset(),
	                height = this.container.outerHeight(false),
	                width = this.container.outerWidth(false),
	                dropHeight = $dropdown.outerHeight(false),
	                $window = $(window),
	                windowWidth = $window.width(),
	                windowHeight = $window.height(),
	                viewPortRight = $window.scrollLeft() + windowWidth,
	                viewportBottom = $window.scrollTop() + windowHeight,
	                dropTop = offset.top + height,
	                dropLeft = offset.left,
	                enoughRoomBelow = dropTop + dropHeight <= viewportBottom,
	                enoughRoomAbove = (offset.top - dropHeight) >= this.body().scrollTop(),
	                dropWidth = $dropdown.outerWidth(false),
	                enoughRoomOnRight = dropLeft + dropWidth <= viewPortRight,
	                aboveNow = $dropdown.hasClass("select2-drop-above"),
	                bodyOffset,
	                above,
	                changeDirection,
	                css,
	                resultsListNode;

	            // always prefer the current above/below alignment, unless there is not enough room
	            if (aboveNow) {
	                above = true;
	                if (!enoughRoomAbove && enoughRoomBelow) {
	                    changeDirection = true;
	                    above = false;
	                }
	            } else {
	                above = false;
	                if (!enoughRoomBelow && enoughRoomAbove) {
	                    changeDirection = true;
	                    above = true;
	                }
	            }

	            //if we are changing direction we need to get positions when dropdown is hidden;
	            if (changeDirection) {
	                $dropdown.hide();
	                offset = this.container.offset();
	                height = this.container.outerHeight(false);
	                width = this.container.outerWidth(false);
	                dropHeight = $dropdown.outerHeight(false);
	                viewPortRight = $window.scrollLeft() + windowWidth;
	                viewportBottom = $window.scrollTop() + windowHeight;
	                dropTop = offset.top + height;
	                dropLeft = offset.left;
	                dropWidth = $dropdown.outerWidth(false);
	                enoughRoomOnRight = dropLeft + dropWidth <= viewPortRight;
	                $dropdown.show();
	            }

	            if (this.opts.dropdownAutoWidth) {
	                resultsListNode = $('.select2-results', $dropdown)[0];
	                $dropdown.addClass('select2-drop-auto-width');
	                $dropdown.css('width', '');
	                // Add scrollbar width to dropdown if vertical scrollbar is present
	                dropWidth = $dropdown.outerWidth(false) + (resultsListNode.scrollHeight === resultsListNode.clientHeight ? 0 : scrollBarDimensions.width);
	                dropWidth > width ? width = dropWidth : dropWidth = width;
	                enoughRoomOnRight = dropLeft + dropWidth <= viewPortRight;
	            }
	            else {
	                this.container.removeClass('select2-drop-auto-width');
	            }

	            //console.log("below/ droptop:", dropTop, "dropHeight", dropHeight, "sum", (dropTop+dropHeight)+" viewport bottom", viewportBottom, "enough?", enoughRoomBelow);
	            //console.log("above/ offset.top", offset.top, "dropHeight", dropHeight, "top", (offset.top-dropHeight), "scrollTop", this.body().scrollTop(), "enough?", enoughRoomAbove);

	            // fix positioning when body has an offset and is not position: static
	            if (this.body().css('position') !== 'static') {
	                bodyOffset = this.body().offset();
	                dropTop -= bodyOffset.top;
	                dropLeft -= bodyOffset.left;
	            }

	            if (!enoughRoomOnRight) {
	               dropLeft = offset.left + width - dropWidth;
	            }

	            css =  {
	                left: dropLeft,
	                width: width
	            };

	            if (above) {
	                css.bottom = windowHeight - offset.top;
	                css.top = 'auto';
	                this.container.addClass("select2-drop-above");
	                $dropdown.addClass("select2-drop-above");
	            }
	            else {
	                css.top = dropTop;
	                css.bottom = 'auto';
	                this.container.removeClass("select2-drop-above");
	                $dropdown.removeClass("select2-drop-above");
	            }
	            css = $.extend(css, evaluate(this.opts.dropdownCss));

	            $dropdown.css(css);
	        },

	        // abstract
	        shouldOpen: function() {
	            var event;

	            if (this.opened()) return false;

	            if (this._enabled === false || this._readonly === true) return false;

	            event = $.Event("select2-opening");
	            this.opts.element.trigger(event);
	            return !event.isDefaultPrevented();
	        },

	        // abstract
	        clearDropdownAlignmentPreference: function() {
	            // clear the classes used to figure out the preference of where the dropdown should be opened
	            this.container.removeClass("select2-drop-above");
	            this.dropdown.removeClass("select2-drop-above");
	        },

	        /**
	         * Opens the dropdown
	         *
	         * @return {Boolean} whether or not dropdown was opened. This method will return false if, for example,
	         * the dropdown is already open, or if the 'open' event listener on the element called preventDefault().
	         */
	        // abstract
	        open: function () {

	            if (!this.shouldOpen()) return false;

	            this.opening();

	            return true;
	        },

	        /**
	         * Performs the opening of the dropdown
	         */
	        // abstract
	        opening: function() {
	            var cid = this.containerId,
	                scroll = "scroll." + cid,
	                resize = "resize."+cid,
	                orient = "orientationchange."+cid,
	                mask;

	            this.container.addClass("select2-dropdown-open").addClass("select2-container-active");

	            this.clearDropdownAlignmentPreference();

	            if(this.dropdown[0] !== this.body().children().last()[0]) {
	                this.dropdown.detach().appendTo(this.body());
	            }

	            // create the dropdown mask if doesnt already exist
	            mask = $("#select2-drop-mask");
	            if (mask.length == 0) {
	                mask = $(document.createElement("div"));
	                mask.attr("id","select2-drop-mask").attr("class","select2-drop-mask");
	                mask.hide();
	                mask.appendTo(this.body());
	                mask.on("mousedown touchstart click", function (e) {
	                    var dropdown = $("#select2-drop"), self;
	                    if (dropdown.length > 0) {
	                        self=dropdown.data("select2");
	                        if (self.opts.selectOnBlur) {
	                            self.selectHighlighted({noFocus: true});
	                        }
	                        self.close({focus:true});
	                        e.preventDefault();
	                        e.stopPropagation();
	                    }
	                });
	            }

	            // ensure the mask is always right before the dropdown
	            if (this.dropdown.prev()[0] !== mask[0]) {
	                this.dropdown.before(mask);
	            }

	            // move the global id to the correct dropdown
	            $("#select2-drop").removeAttr("id");
	            this.dropdown.attr("id", "select2-drop");

	            // show the elements
	            mask.show();

	            this.positionDropdown();
	            this.dropdown.show();
	            this.positionDropdown();

	            this.dropdown.addClass("select2-drop-active");

	            // attach listeners to events that can change the position of the container and thus require
	            // the position of the dropdown to be updated as well so it does not come unglued from the container
	            var that = this;
	            this.container.parents().add(window).each(function () {
	                $(this).on(resize+" "+scroll+" "+orient, function (e) {
	                    that.positionDropdown();
	                });
	            });


	        },

	        // abstract
	        close: function () {
	            if (!this.opened()) return;

	            var cid = this.containerId,
	                scroll = "scroll." + cid,
	                resize = "resize."+cid,
	                orient = "orientationchange."+cid;

	            // unbind event listeners
	            this.container.parents().add(window).each(function () { $(this).off(scroll).off(resize).off(orient); });

	            this.clearDropdownAlignmentPreference();

	            $("#select2-drop-mask").hide();
	            this.dropdown.removeAttr("id"); // only the active dropdown has the select2-drop id
	            this.dropdown.hide();
	            this.container.removeClass("select2-dropdown-open").removeClass("select2-container-active");
	            this.results.empty();


	            this.clearSearch();
	            this.search.removeClass("select2-active");
	            this.opts.element.trigger($.Event("select2-close"));
	        },

	        /**
	         * Opens control, sets input value, and updates results.
	         */
	        // abstract
	        externalSearch: function (term) {
	            this.open();
	            this.search.val(term);
	            this.updateResults(false);
	        },

	        // abstract
	        clearSearch: function () {

	        },

	        //abstract
	        getMaximumSelectionSize: function() {
	            return evaluate(this.opts.maximumSelectionSize);
	        },

	        // abstract
	        ensureHighlightVisible: function () {
	            var results = this.results, children, index, child, hb, rb, y, more;

	            index = this.highlight();

	            if (index < 0) return;

	            if (index == 0) {

	                // if the first element is highlighted scroll all the way to the top,
	                // that way any unselectable headers above it will also be scrolled
	                // into view

	                results.scrollTop(0);
	                return;
	            }

	            children = this.findHighlightableChoices().find('.select2-result-label');

	            child = $(children[index]);

	            hb = child.offset().top + child.outerHeight(true);

	            // if this is the last child lets also make sure select2-more-results is visible
	            if (index === children.length - 1) {
	                more = results.find("li.select2-more-results");
	                if (more.length > 0) {
	                    hb = more.offset().top + more.outerHeight(true);
	                }
	            }

	            rb = results.offset().top + results.outerHeight(true);
	            if (hb > rb) {
	                results.scrollTop(results.scrollTop() + (hb - rb));
	            }
	            y = child.offset().top - results.offset().top;

	            // make sure the top of the element is visible
	            if (y < 0 && child.css('display') != 'none' ) {
	                results.scrollTop(results.scrollTop() + y); // y is negative
	            }
	        },

	        // abstract
	        findHighlightableChoices: function() {
	            return this.results.find(".select2-result-selectable:not(.select2-disabled, .select2-selected)");
	        },

	        // abstract
	        moveHighlight: function (delta) {
	            var choices = this.findHighlightableChoices(),
	                index = this.highlight();

	            while (index > -1 && index < choices.length) {
	                index += delta;
	                var choice = $(choices[index]);
	                if (choice.hasClass("select2-result-selectable") && !choice.hasClass("select2-disabled") && !choice.hasClass("select2-selected")) {
	                    this.highlight(index);
	                    break;
	                }
	            }
	        },

	        // abstract
	        highlight: function (index) {
	            var choices = this.findHighlightableChoices(),
	                choice,
	                data;

	            if (arguments.length === 0) {
	                return indexOf(choices.filter(".select2-highlighted")[0], choices.get());
	            }

	            if (index >= choices.length) index = choices.length - 1;
	            if (index < 0) index = 0;

	            this.removeHighlight();

	            choice = $(choices[index]);
	            choice.addClass("select2-highlighted");

	            this.ensureHighlightVisible();

	            data = choice.data("select2-data");
	            if (data) {
	                this.opts.element.trigger({ type: "select2-highlight", val: this.id(data), choice: data });
	            }
	        },

	        removeHighlight: function() {
	            this.results.find(".select2-highlighted").removeClass("select2-highlighted");
	        },

	        // abstract
	        countSelectableResults: function() {
	            return this.findHighlightableChoices().length;
	        },

	        // abstract
	        highlightUnderEvent: function (event) {
	            var el = $(event.target).closest(".select2-result-selectable");
	            if (el.length > 0 && !el.is(".select2-highlighted")) {
	                var choices = this.findHighlightableChoices();
	                this.highlight(choices.index(el));
	            } else if (el.length == 0) {
	                // if we are over an unselectable item remove all highlights
	                this.removeHighlight();
	            }
	        },

	        // abstract
	        loadMoreIfNeeded: function () {
	            var results = this.results,
	                more = results.find("li.select2-more-results"),
	                below, // pixels the element is below the scroll fold, below==0 is when the element is starting to be visible
	                page = this.resultsPage + 1,
	                self=this,
	                term=this.search.val(),
	                context=this.context;

	            if (more.length === 0) return;
	            below = more.offset().top - results.offset().top - results.height();

	            if (below <= this.opts.loadMorePadding) {
	                more.addClass("select2-active");
	                this.opts.query({
	                        element: this.opts.element,
	                        term: term,
	                        page: page,
	                        context: context,
	                        matcher: this.opts.matcher,
	                        callback: this.bind(function (data) {

	                    // ignore a response if the select2 has been closed before it was received
	                    if (!self.opened()) return;


	                    self.opts.populateResults.call(this, results, data.results, {term: term, page: page, context:context});
	                    self.postprocessResults(data, false, false);

	                    if (data.more===true) {
	                        more.detach().appendTo(results).text(self.opts.formatLoadMore(page+1));
	                        window.setTimeout(function() { self.loadMoreIfNeeded(); }, 10);
	                    } else {
	                        more.remove();
	                    }
	                    self.positionDropdown();
	                    self.resultsPage = page;
	                    self.context = data.context;
	                    this.opts.element.trigger({ type: "select2-loaded", items: data });
	                })});
	            }
	        },

	        /**
	         * Default tokenizer function which does nothing
	         */
	        tokenize: function() {

	        },

	        /**
	         * @param initial whether or not this is the call to this method right after the dropdown has been opened
	         */
	        // abstract
	        updateResults: function (initial) {
	            var search = this.search,
	                results = this.results,
	                opts = this.opts,
	                data,
	                self = this,
	                input,
	                term = search.val(),
	                lastTerm = $.data(this.container, "select2-last-term"),
	                // sequence number used to drop out-of-order responses
	                queryNumber;

	            // prevent duplicate queries against the same term
	            if (initial !== true && lastTerm && equal(term, lastTerm)) return;

	            $.data(this.container, "select2-last-term", term);

	            // if the search is currently hidden we do not alter the results
	            if (initial !== true && (this.showSearchInput === false || !this.opened())) {
	                return;
	            }

	            function postRender() {
	                search.removeClass("select2-active");
	                self.positionDropdown();
	            }

	            function render(html) {
	                results.html(html);
	                postRender();
	            }

	            queryNumber = ++this.queryCount;

	            var maxSelSize = this.getMaximumSelectionSize();
	            if (maxSelSize >=1) {
	                data = this.data();
	                if ($.isArray(data) && data.length >= maxSelSize && checkFormatter(opts.formatSelectionTooBig, "formatSelectionTooBig")) {
	                    render("<li class='select2-selection-limit'>" + opts.formatSelectionTooBig(maxSelSize) + "</li>");
	                    return;
	                }
	            }

	            if (search.val().length < opts.minimumInputLength) {
	                if (checkFormatter(opts.formatInputTooShort, "formatInputTooShort")) {
	                    render("<li class='select2-no-results'>" + opts.formatInputTooShort(search.val(), opts.minimumInputLength) + "</li>");
	                } else {
	                    render("");
	                }
	                if (initial && this.showSearch) this.showSearch(true);
	                return;
	            }

	            if (opts.maximumInputLength && search.val().length > opts.maximumInputLength) {
	                if (checkFormatter(opts.formatInputTooLong, "formatInputTooLong")) {
	                    render("<li class='select2-no-results'>" + opts.formatInputTooLong(search.val(), opts.maximumInputLength) + "</li>");
	                } else {
	                    render("");
	                }
	                return;
	            }

	            if (opts.formatSearching && this.findHighlightableChoices().length === 0) {
	                render("<li class='select2-searching'>" + opts.formatSearching() + "</li>");
	            }

	            search.addClass("select2-active");

	            this.removeHighlight();

	            // give the tokenizer a chance to pre-process the input
	            input = this.tokenize();
	            if (input != undefined && input != null) {
	                search.val(input);
	            }

	            this.resultsPage = 1;

	            opts.query({
	                element: opts.element,
	                    term: search.val(),
	                    page: this.resultsPage,
	                    context: null,
	                    matcher: opts.matcher,
	                    callback: this.bind(function (data) {
	                var def; // default choice

	                // ignore old responses
	                if (queryNumber != this.queryCount) {
	                  return;
	                }

	                // ignore a response if the select2 has been closed before it was received
	                if (!this.opened()) {
	                    this.search.removeClass("select2-active");
	                    return;
	                }

	                // save context, if any
	                this.context = (data.context===undefined) ? null : data.context;
	                // create a default choice and prepend it to the list
	                if (this.opts.createSearchChoice && search.val() !== "") {
	                    def = this.opts.createSearchChoice.call(self, search.val(), data.results);
	                    if (def !== undefined && def !== null && self.id(def) !== undefined && self.id(def) !== null) {
	                        if ($(data.results).filter(
	                            function () {
	                                return equal(self.id(this), self.id(def));
	                            }).length === 0) {
	                            data.results.unshift(def);
	                        }
	                    }
	                }

	                if (data.results.length === 0 && checkFormatter(opts.formatNoMatches, "formatNoMatches")) {
	                    render("<li class='select2-no-results'>" + opts.formatNoMatches(search.val()) + "</li>");
	                    return;
	                }

	                results.empty();
	                self.opts.populateResults.call(this, results, data.results, {term: search.val(), page: this.resultsPage, context:null});

	                if (data.more === true && checkFormatter(opts.formatLoadMore, "formatLoadMore")) {
	                    results.append("<li class='select2-more-results'>" + self.opts.escapeMarkup(opts.formatLoadMore(this.resultsPage)) + "</li>");
	                    window.setTimeout(function() { self.loadMoreIfNeeded(); }, 10);
	                }

	                this.postprocessResults(data, initial);

	                postRender();

	                this.opts.element.trigger({ type: "select2-loaded", items: data });
	            })});
	        },

	        // abstract
	        cancel: function () {
	            this.close();
	        },

	        // abstract
	        blur: function () {
	            // if selectOnBlur == true, select the currently highlighted option
	            if (this.opts.selectOnBlur)
	                this.selectHighlighted({noFocus: true});

	            this.close();
	            this.container.removeClass("select2-container-active");
	            // synonymous to .is(':focus'), which is available in jquery >= 1.6
	            if (this.search[0] === document.activeElement) { this.search.blur(); }
	            this.clearSearch();
	            this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus");
	        },

	        // abstract
	        focusSearch: function () {
	            focus(this.search);
	        },

	        // abstract
	        selectHighlighted: function (options) {
	            var index=this.highlight(),
	                highlighted=this.results.find(".select2-highlighted"),
	                data = highlighted.closest('.select2-result').data("select2-data");

	            if (data) {
	                this.highlight(index);
	                this.onSelect(data, options);
	            } else if (options && options.noFocus) {
	                this.close();
	            }
	        },

	        // abstract
	        getPlaceholder: function () {
	            var placeholderOption;
	            return this.opts.element.attr("placeholder") ||
	                this.opts.element.attr("data-placeholder") || // jquery 1.4 compat
	                this.opts.element.data("placeholder") ||
	                this.opts.placeholder ||
	                ((placeholderOption = this.getPlaceholderOption()) !== undefined ? placeholderOption.text() : undefined);
	        },

	        // abstract
	        getPlaceholderOption: function() {
	            if (this.select) {
	                var firstOption = this.select.children('option').first();
	                if (this.opts.placeholderOption !== undefined ) {
	                    //Determine the placeholder option based on the specified placeholderOption setting
	                    return (this.opts.placeholderOption === "first" && firstOption) ||
	                           (typeof this.opts.placeholderOption === "function" && this.opts.placeholderOption(this.select));
	                } else if (firstOption.text() === "" && firstOption.val() === "") {
	                    //No explicit placeholder option specified, use the first if it's blank
	                    return firstOption;
	                }
	            }
	        },

	        /**
	         * Get the desired width for the container element.  This is
	         * derived first from option `width` passed to select2, then
	         * the inline 'style' on the original element, and finally
	         * falls back to the jQuery calculated element width.
	         */
	        // abstract
	        initContainerWidth: function () {
	            function resolveContainerWidth() {
	                var style, attrs, matches, i, l, attr;

	                if (this.opts.width === "off") {
	                    return null;
	                } else if (this.opts.width === "element"){
	                    return this.opts.element.outerWidth(false) === 0 ? 'auto' : this.opts.element.outerWidth(false) + 'px';
	                } else if (this.opts.width === "copy" || this.opts.width === "resolve") {
	                    // check if there is inline style on the element that contains width
	                    style = this.opts.element.attr('style');
	                    if (style !== undefined) {
	                        attrs = style.split(';');
	                        for (i = 0, l = attrs.length; i < l; i = i + 1) {
	                            attr = attrs[i].replace(/\s/g, '');
	                            matches = attr.match(/^width:(([-+]?([0-9]*\.)?[0-9]+)(px|em|ex|%|in|cm|mm|pt|pc))/i);
	                            if (matches !== null && matches.length >= 1)
	                                return matches[1];
	                        }
	                    }

	                    if (this.opts.width === "resolve") {
	                        // next check if css('width') can resolve a width that is percent based, this is sometimes possible
	                        // when attached to input type=hidden or elements hidden via css
	                        style = this.opts.element.css('width');
	                        if (style.indexOf("%") > 0) return style;

	                        // finally, fallback on the calculated width of the element
	                        return (this.opts.element.outerWidth(false) === 0 ? 'auto' : this.opts.element.outerWidth(false) + 'px');
	                    }

	                    return null;
	                } else if ($.isFunction(this.opts.width)) {
	                    return this.opts.width();
	                } else {
	                    return this.opts.width;
	               }
	            };

	            var width = resolveContainerWidth.call(this);
	            if (width !== null) {
	                this.container.css("width", width);
	            }
	        }
	    });

	    SingleSelect2 = clazz(AbstractSelect2, {

	        // single

	        createContainer: function () {
	            var container = $(document.createElement("div")).attr({
	                "class": "select2-container"
	            }).html([
	                "<a href='javascript:void(0)' onclick='return false;' class='select2-choice' tabindex='-1'>",
	                "   <span class='select2-chosen'>&nbsp;</span><abbr class='select2-search-choice-close'></abbr>",
	                "   <span class='select2-arrow'><b></b></span>",
	                "</a>",
	                "<input class='select2-focusser select2-offscreen' type='text'/>",
	                "<div class='select2-drop select2-display-none'>",
	                "   <div class='select2-search'>",
	                "       <input type='text' autocomplete='off' autocorrect='off' autocapitalize='off' spellcheck='false' class='select2-input'/>",
	                "   </div>",
	                "   <ul class='select2-results'>",
	                "   </ul>",
	                "</div>"].join(""));
	            return container;
	        },

	        // single
	        enableInterface: function() {
	            if (this.parent.enableInterface.apply(this, arguments)) {
	                this.focusser.prop("disabled", !this.isInterfaceEnabled());
	            }
	        },

	        // single
	        opening: function () {
	            var el, range, len;

	            if (this.opts.minimumResultsForSearch >= 0) {
	                this.showSearch(true);
	            }

	            this.parent.opening.apply(this, arguments);

	            if (this.showSearchInput !== false) {
	                // IE appends focusser.val() at the end of field :/ so we manually insert it at the beginning using a range
	                // all other browsers handle this just fine

	                this.search.val(this.focusser.val());
	            }
	            this.search.focus();
	            // move the cursor to the end after focussing, otherwise it will be at the beginning and
	            // new text will appear *before* focusser.val()
	            el = this.search.get(0);
	            if (el.createTextRange) {
	                range = el.createTextRange();
	                range.collapse(false);
	                range.select();
	            } else if (el.setSelectionRange) {
	                len = this.search.val().length;
	                el.setSelectionRange(len, len);
	            }

	            // initializes search's value with nextSearchTerm (if defined by user)
	            // ignore nextSearchTerm if the dropdown is opened by the user pressing a letter
	            if(this.search.val() === "") {
	                if(this.nextSearchTerm != undefined){
	                    this.search.val(this.nextSearchTerm);
	                    this.search.select();
	                }
	            }

	            this.focusser.prop("disabled", true).val("");
	            this.updateResults(true);
	            this.opts.element.trigger($.Event("select2-open"));
	        },

	        // single
	        close: function (params) {
	            if (!this.opened()) return;
	            this.parent.close.apply(this, arguments);

	            params = params || {focus: true};
	            this.focusser.removeAttr("disabled");

	            if (params.focus) {
	                this.focusser.focus();
	            }
	        },

	        // single
	        focus: function () {
	            if (this.opened()) {
	                this.close();
	            } else {
	                this.focusser.removeAttr("disabled");
	                this.focusser.focus();
	            }
	        },

	        // single
	        isFocused: function () {
	            return this.container.hasClass("select2-container-active");
	        },

	        // single
	        cancel: function () {
	            this.parent.cancel.apply(this, arguments);
	            this.focusser.removeAttr("disabled");
	            this.focusser.focus();
	        },

	        // single
	        destroy: function() {
	            $("label[for='" + this.focusser.attr('id') + "']")
	                .attr('for', this.opts.element.attr("id"));
	            this.parent.destroy.apply(this, arguments);
	        },

	        // single
	        initContainer: function () {

	            var selection,
	                container = this.container,
	                dropdown = this.dropdown;

	            if (this.opts.minimumResultsForSearch < 0) {
	                this.showSearch(false);
	            } else {
	                this.showSearch(true);
	            }

	            this.selection = selection = container.find(".select2-choice");

	            this.focusser = container.find(".select2-focusser");

	            // rewrite labels from original element to focusser
	            this.focusser.attr("id", "s2id_autogen"+nextUid());

	            $("label[for='" + this.opts.element.attr("id") + "']")
	                .attr('for', this.focusser.attr('id'));

	            this.focusser.attr("tabindex", this.elementTabIndex);

	            this.search.on("keydown", this.bind(function (e) {
	                if (!this.isInterfaceEnabled()) return;

	                if (e.which === KEY.PAGE_UP || e.which === KEY.PAGE_DOWN) {
	                    // prevent the page from scrolling
	                    killEvent(e);
	                    return;
	                }

	                switch (e.which) {
	                    case KEY.UP:
	                    case KEY.DOWN:
	                        this.moveHighlight((e.which === KEY.UP) ? -1 : 1);
	                        killEvent(e);
	                        return;
	                    case KEY.ENTER:
	                        this.selectHighlighted();
	                        killEvent(e);
	                        return;
	                    case KEY.TAB:
	                        this.selectHighlighted({noFocus: true});
	                        return;
	                    case KEY.ESC:
	                        this.cancel(e);
	                        killEvent(e);
	                        return;
	                }
	            }));

	            this.search.on("blur", this.bind(function(e) {
	                // a workaround for chrome to keep the search field focussed when the scroll bar is used to scroll the dropdown.
	                // without this the search field loses focus which is annoying
	                if (document.activeElement === this.body().get(0)) {
	                    window.setTimeout(this.bind(function() {
	                        this.search.focus();
	                    }), 0);
	                }
	            }));

	            this.focusser.on("keydown", this.bind(function (e) {
	                if (!this.isInterfaceEnabled()) return;

	                if (e.which === KEY.TAB || KEY.isControl(e) || KEY.isFunctionKey(e) || e.which === KEY.ESC) {
	                    return;
	                }

	                if (this.opts.openOnEnter === false && e.which === KEY.ENTER) {
	                    killEvent(e);
	                    return;
	                }

	                if (e.which == KEY.DOWN || e.which == KEY.UP
	                    || (e.which == KEY.ENTER && this.opts.openOnEnter)) {

	                    if (e.altKey || e.ctrlKey || e.shiftKey || e.metaKey) return;

	                    this.open();
	                    killEvent(e);
	                    return;
	                }

	                if (e.which == KEY.DELETE || e.which == KEY.BACKSPACE) {
	                    if (this.opts.allowClear) {
	                        this.clear();
	                    }
	                    killEvent(e);
	                    return;
	                }
	            }));


	            installKeyUpChangeEvent(this.focusser);
	            this.focusser.on("keyup-change input", this.bind(function(e) {
	                if (this.opts.minimumResultsForSearch >= 0) {
	                    e.stopPropagation();
	                    if (this.opened()) return;
	                    this.open();
	                }
	            }));

	            selection.on("mousedown", "abbr", this.bind(function (e) {
	                if (!this.isInterfaceEnabled()) return;
	                this.clear();
	                killEventImmediately(e);
	                this.close();
	                this.selection.focus();
	            }));

	            selection.on("mousedown", this.bind(function (e) {

	                if (!this.container.hasClass("select2-container-active")) {
	                    this.opts.element.trigger($.Event("select2-focus"));
	                }

	                if (this.opened()) {
	                    this.close();
	                } else if (this.isInterfaceEnabled()) {
	                    this.open();
	                }

	                killEvent(e);
	            }));

	            dropdown.on("mousedown", this.bind(function() { this.search.focus(); }));

	            selection.on("focus", this.bind(function(e) {
	                killEvent(e);
	            }));

	            this.focusser.on("focus", this.bind(function(){
	                if (!this.container.hasClass("select2-container-active")) {
	                    this.opts.element.trigger($.Event("select2-focus"));
	                }
	                this.container.addClass("select2-container-active");
	            })).on("blur", this.bind(function() {
	                if (!this.opened()) {
	                    this.container.removeClass("select2-container-active");
	                    this.opts.element.trigger($.Event("select2-blur"));
	                }
	            }));
	            this.search.on("focus", this.bind(function(){
	                if (!this.container.hasClass("select2-container-active")) {
	                    this.opts.element.trigger($.Event("select2-focus"));
	                }
	                this.container.addClass("select2-container-active");
	            }));

	            this.initContainerWidth();
	            this.opts.element.addClass("select2-offscreen");
	            this.setPlaceholder();

	        },

	        // single
	        clear: function(triggerChange) {
	            var data=this.selection.data("select2-data");
	            if (data) { // guard against queued quick consecutive clicks
	                var evt = $.Event("select2-clearing");
	                this.opts.element.trigger(evt);
	                if (evt.isDefaultPrevented()) {
	                    return;
	                }
	                var placeholderOption = this.getPlaceholderOption();
	                this.opts.element.val(placeholderOption ? placeholderOption.val() : "");
	                this.selection.find(".select2-chosen").empty();
	                this.selection.removeData("select2-data");
	                this.setPlaceholder();

	                if (triggerChange !== false){
	                    this.opts.element.trigger({ type: "select2-removed", val: this.id(data), choice: data });
	                    this.triggerChange({removed:data});
	                }
	            }
	        },

	        /**
	         * Sets selection based on source element's value
	         */
	        // single
	        initSelection: function () {
	            var selected;
	            if (this.isPlaceholderOptionSelected()) {
	                this.updateSelection(null);
	                this.close();
	                this.setPlaceholder();
	            } else {
	                var self = this;
	                this.opts.initSelection.call(null, this.opts.element, function(selected){
	                    if (selected !== undefined && selected !== null) {
	                        self.updateSelection(selected);
	                        self.close();
	                        self.setPlaceholder();
	                    }
	                });
	            }
	        },

	        isPlaceholderOptionSelected: function() {
	            var placeholderOption;
	            if (!this.getPlaceholder()) return false; // no placeholder specified so no option should be considered
	            return ((placeholderOption = this.getPlaceholderOption()) !== undefined && placeholderOption.prop("selected"))
	                || (this.opts.element.val() === "")
	                || (this.opts.element.val() === undefined)
	                || (this.opts.element.val() === null);
	        },

	        // single
	        prepareOpts: function () {
	            var opts = this.parent.prepareOpts.apply(this, arguments),
	                self=this;

	            if (opts.element.get(0).tagName.toLowerCase() === "select") {
	                // install the selection initializer
	                opts.initSelection = function (element, callback) {
	                    var selected = element.find("option").filter(function() { return this.selected });
	                    // a single select box always has a value, no need to null check 'selected'
	                    callback(self.optionToData(selected));
	                };
	            } else if ("data" in opts) {
	                // install default initSelection when applied to hidden input and data is local
	                opts.initSelection = opts.initSelection || function (element, callback) {
	                    var id = element.val();
	                    //search in data by id, storing the actual matching item
	                    var match = null;
	                    opts.query({
	                        matcher: function(term, text, el){
	                            var is_match = equal(id, opts.id(el));
	                            if (is_match) {
	                                match = el;
	                            }
	                            return is_match;
	                        },
	                        callback: !$.isFunction(callback) ? $.noop : function() {
	                            callback(match);
	                        }
	                    });
	                };
	            }

	            return opts;
	        },

	        // single
	        getPlaceholder: function() {
	            // if a placeholder is specified on a single select without a valid placeholder option ignore it
	            if (this.select) {
	                if (this.getPlaceholderOption() === undefined) {
	                    return undefined;
	                }
	            }

	            return this.parent.getPlaceholder.apply(this, arguments);
	        },

	        // single
	        setPlaceholder: function () {
	            var placeholder = this.getPlaceholder();

	            if (this.isPlaceholderOptionSelected() && placeholder !== undefined) {

	                // check for a placeholder option if attached to a select
	                if (this.select && this.getPlaceholderOption() === undefined) return;

	                this.selection.find(".select2-chosen").html(this.opts.escapeMarkup(placeholder));

	                this.selection.addClass("select2-default");

	                this.container.removeClass("select2-allowclear");
	            }
	        },

	        // single
	        postprocessResults: function (data, initial, noHighlightUpdate) {
	            var selected = 0, self = this, showSearchInput = true;

	            // find the selected element in the result list

	            this.findHighlightableChoices().each2(function (i, elm) {
	                if (equal(self.id(elm.data("select2-data")), self.opts.element.val())) {
	                    selected = i;
	                    return false;
	                }
	            });

	            // and highlight it
	            if (noHighlightUpdate !== false) {
	                if (initial === true && selected >= 0) {
	                    this.highlight(selected);
	                } else {
	                    this.highlight(0);
	                }
	            }

	            // hide the search box if this is the first we got the results and there are enough of them for search

	            if (initial === true) {
	                var min = this.opts.minimumResultsForSearch;
	                if (min >= 0) {
	                    this.showSearch(countResults(data.results) >= min);
	                }
	            }
	        },

	        // single
	        showSearch: function(showSearchInput) {
	            if (this.showSearchInput === showSearchInput) return;

	            this.showSearchInput = showSearchInput;

	            this.dropdown.find(".select2-search").toggleClass("select2-search-hidden", !showSearchInput);
	            this.dropdown.find(".select2-search").toggleClass("select2-offscreen", !showSearchInput);
	            //add "select2-with-searchbox" to the container if search box is shown
	            $(this.dropdown, this.container).toggleClass("select2-with-searchbox", showSearchInput);
	        },

	        // single
	        onSelect: function (data, options) {

	            if (!this.triggerSelect(data)) { return; }

	            var old = this.opts.element.val(),
	                oldData = this.data();

	            this.opts.element.val(this.id(data));
	            this.updateSelection(data);

	            this.opts.element.trigger({ type: "select2-selected", val: this.id(data), choice: data });

	            this.nextSearchTerm = this.opts.nextSearchTerm(data, this.search.val());
	            this.close();

	            if (!options || !options.noFocus)
	                this.focusser.focus();

	            if (!equal(old, this.id(data))) { this.triggerChange({added:data,removed:oldData}); }
	        },

	        // single
	        updateSelection: function (data) {

	            var container=this.selection.find(".select2-chosen"), formatted, cssClass;

	            this.selection.data("select2-data", data);

	            container.empty();
	            if (data !== null) {
	                formatted=this.opts.formatSelection(data, container, this.opts.escapeMarkup);
	            }
	            if (formatted !== undefined) {
	                container.append(formatted);
	            }
	            cssClass=this.opts.formatSelectionCssClass(data, container);
	            if (cssClass !== undefined) {
	                container.addClass(cssClass);
	            }

	            this.selection.removeClass("select2-default");

	            if (this.opts.allowClear && this.getPlaceholder() !== undefined) {
	                this.container.addClass("select2-allowclear");
	            }
	        },

	        // single
	        val: function () {
	            var val,
	                triggerChange = false,
	                data = null,
	                self = this,
	                oldData = this.data();

	            if (arguments.length === 0) {
	                return this.opts.element.val();
	            }

	            val = arguments[0];

	            if (arguments.length > 1) {
	                triggerChange = arguments[1];
	            }

	            if (this.select) {
	                this.select
	                    .val(val)
	                    .find("option").filter(function() { return this.selected }).each2(function (i, elm) {
	                        data = self.optionToData(elm);
	                        return false;
	                    });
	                this.updateSelection(data);
	                this.setPlaceholder();
	                if (triggerChange) {
	                    this.triggerChange({added: data, removed:oldData});
	                }
	            } else {
	                // val is an id. !val is true for [undefined,null,'',0] - 0 is legal
	                if (!val && val !== 0) {
	                    this.clear(triggerChange);
	                    return;
	                }
	                if (this.opts.initSelection === undefined) {
	                    throw new Error("cannot call val() if initSelection() is not defined");
	                }
	                this.opts.element.val(val);
	                this.opts.initSelection(this.opts.element, function(data){
	                    self.opts.element.val(!data ? "" : self.id(data));
	                    self.updateSelection(data);
	                    self.setPlaceholder();
	                    if (triggerChange) {
	                        self.triggerChange({added: data, removed:oldData});
	                    }
	                });
	            }
	        },

	        // single
	        clearSearch: function () {
	            this.search.val("");
	            this.focusser.val("");
	        },

	        // single
	        data: function(value) {
	            var data,
	                triggerChange = false;

	            if (arguments.length === 0) {
	                data = this.selection.data("select2-data");
	                if (data == undefined) data = null;
	                return data;
	            } else {
	                if (arguments.length > 1) {
	                    triggerChange = arguments[1];
	                }
	                if (!value) {
	                    this.clear(triggerChange);
	                } else {
	                    data = this.data();
	                    this.opts.element.val(!value ? "" : this.id(value));
	                    this.updateSelection(value);
	                    if (triggerChange) {
	                        this.triggerChange({added: value, removed:data});
	                    }
	                }
	            }
	        }
	    });

	    MultiSelect2 = clazz(AbstractSelect2, {

	        // multi
	        createContainer: function () {
	            var container = $(document.createElement("div")).attr({
	                "class": "select2-container select2-container-multi"
	            }).html([
	                "<ul class='select2-choices'>",
	                "  <li class='select2-search-field'>",
	                "    <input type='text' autocomplete='off' autocorrect='off' autocapitalize='off' spellcheck='false' class='select2-input'>",
	                "  </li>",
	                "</ul>",
	                "<div class='select2-drop select2-drop-multi select2-display-none'>",
	                "   <ul class='select2-results'>",
	                "   </ul>",
	                "</div>"].join(""));
	            return container;
	        },

	        // multi
	        prepareOpts: function () {
	            var opts = this.parent.prepareOpts.apply(this, arguments),
	                self=this;

	            // TODO validate placeholder is a string if specified

	            if (opts.element.get(0).tagName.toLowerCase() === "select") {
	                // install sthe selection initializer
	                opts.initSelection = function (element, callback) {

	                    var data = [];

	                    element.find("option").filter(function() { return this.selected }).each2(function (i, elm) {
	                        data.push(self.optionToData(elm));
	                    });
	                    callback(data);
	                };
	            } else if ("data" in opts) {
	                // install default initSelection when applied to hidden input and data is local
	                opts.initSelection = opts.initSelection || function (element, callback) {
	                    var ids = splitVal(element.val(), opts.separator);
	                    //search in data by array of ids, storing matching items in a list
	                    var matches = [];
	                    opts.query({
	                        matcher: function(term, text, el){
	                            var is_match = $.grep(ids, function(id) {
	                                return equal(id, opts.id(el));
	                            }).length;
	                            if (is_match) {
	                                matches.push(el);
	                            }
	                            return is_match;
	                        },
	                        callback: !$.isFunction(callback) ? $.noop : function() {
	                            // reorder matches based on the order they appear in the ids array because right now
	                            // they are in the order in which they appear in data array
	                            var ordered = [];
	                            for (var i = 0; i < ids.length; i++) {
	                                var id = ids[i];
	                                for (var j = 0; j < matches.length; j++) {
	                                    var match = matches[j];
	                                    if (equal(id, opts.id(match))) {
	                                        ordered.push(match);
	                                        matches.splice(j, 1);
	                                        break;
	                                    }
	                                }
	                            }
	                            callback(ordered);
	                        }
	                    });
	                };
	            }

	            return opts;
	        },

	        // multi
	        selectChoice: function (choice) {

	            var selected = this.container.find(".select2-search-choice-focus");
	            if (selected.length && choice && choice[0] == selected[0]) {

	            } else {
	                if (selected.length) {
	                    this.opts.element.trigger("choice-deselected", selected);
	                }
	                selected.removeClass("select2-search-choice-focus");
	                if (choice && choice.length) {
	                    this.close();
	                    choice.addClass("select2-search-choice-focus");
	                    this.opts.element.trigger("choice-selected", choice);
	                }
	            }
	        },

	        // multi
	        destroy: function() {
	            $("label[for='" + this.search.attr('id') + "']")
	                .attr('for', this.opts.element.attr("id"));
	            this.parent.destroy.apply(this, arguments);
	        },

	        // multi
	        initContainer: function () {

	            var selector = ".select2-choices", selection;

	            this.searchContainer = this.container.find(".select2-search-field");
	            this.selection = selection = this.container.find(selector);

	            var _this = this;
	            this.selection.on("click", ".select2-search-choice:not(.select2-locked)", function (e) {
	                //killEvent(e);
	                _this.search[0].focus();
	                _this.selectChoice($(this));
	            });

	            // rewrite labels from original element to focusser
	            this.search.attr("id", "s2id_autogen"+nextUid());
	            $("label[for='" + this.opts.element.attr("id") + "']")
	                .attr('for', this.search.attr('id'));

	            this.search.on("input paste", this.bind(function() {
	                if (!this.isInterfaceEnabled()) return;
	                if (!this.opened()) {
	                    this.open();
	                }
	            }));

	            this.search.attr("tabindex", this.elementTabIndex);

	            this.keydowns = 0;
	            this.search.on("keydown", this.bind(function (e) {
	                if (!this.isInterfaceEnabled()) return;

	                ++this.keydowns;
	                var selected = selection.find(".select2-search-choice-focus");
	                var prev = selected.prev(".select2-search-choice:not(.select2-locked)");
	                var next = selected.next(".select2-search-choice:not(.select2-locked)");
	                var pos = getCursorInfo(this.search);

	                if (selected.length &&
	                    (e.which == KEY.LEFT || e.which == KEY.RIGHT || e.which == KEY.BACKSPACE || e.which == KEY.DELETE || e.which == KEY.ENTER)) {
	                    var selectedChoice = selected;
	                    if (e.which == KEY.LEFT && prev.length) {
	                        selectedChoice = prev;
	                    }
	                    else if (e.which == KEY.RIGHT) {
	                        selectedChoice = next.length ? next : null;
	                    }
	                    else if (e.which === KEY.BACKSPACE) {
	                        this.unselect(selected.first());
	                        this.search.width(10);
	                        selectedChoice = prev.length ? prev : next;
	                    } else if (e.which == KEY.DELETE) {
	                        this.unselect(selected.first());
	                        this.search.width(10);
	                        selectedChoice = next.length ? next : null;
	                    } else if (e.which == KEY.ENTER) {
	                        selectedChoice = null;
	                    }

	                    this.selectChoice(selectedChoice);
	                    killEvent(e);
	                    if (!selectedChoice || !selectedChoice.length) {
	                        this.open();
	                    }
	                    return;
	                } else if (((e.which === KEY.BACKSPACE && this.keydowns == 1)
	                    || e.which == KEY.LEFT) && (pos.offset == 0 && !pos.length)) {

	                    this.selectChoice(selection.find(".select2-search-choice:not(.select2-locked)").last());
	                    killEvent(e);
	                    return;
	                } else {
	                    this.selectChoice(null);
	                }

	                if (this.opened()) {
	                    switch (e.which) {
	                    case KEY.UP:
	                    case KEY.DOWN:
	                        this.moveHighlight((e.which === KEY.UP) ? -1 : 1);
	                        killEvent(e);
	                        return;
	                    case KEY.ENTER:
	                        this.selectHighlighted();
	                        killEvent(e);
	                        return;
	                    case KEY.TAB:
	                        this.selectHighlighted({noFocus:true});
	                        this.close();
	                        return;
	                    case KEY.ESC:
	                        this.cancel(e);
	                        killEvent(e);
	                        return;
	                    }
	                }

	                if (e.which === KEY.TAB || KEY.isControl(e) || KEY.isFunctionKey(e)
	                 || e.which === KEY.BACKSPACE || e.which === KEY.ESC) {
	                    return;
	                }

	                if (e.which === KEY.ENTER) {
	                    if (this.opts.openOnEnter === false) {
	                        return;
	                    } else if (e.altKey || e.ctrlKey || e.shiftKey || e.metaKey) {
	                        return;
	                    }
	                }

	                this.open();

	                if (e.which === KEY.PAGE_UP || e.which === KEY.PAGE_DOWN) {
	                    // prevent the page from scrolling
	                    killEvent(e);
	                }

	                if (e.which === KEY.ENTER) {
	                    // prevent form from being submitted
	                    killEvent(e);
	                }

	            }));

	            this.search.on("keyup", this.bind(function (e) {
	                this.keydowns = 0;
	                this.resizeSearch();
	            })
	            );

	            this.search.on("blur", this.bind(function(e) {
	                this.container.removeClass("select2-container-active");
	                this.search.removeClass("select2-focused");
	                this.selectChoice(null);
	                if (!this.opened()) this.clearSearch();
	                e.stopImmediatePropagation();
	                this.opts.element.trigger($.Event("select2-blur"));
	            }));

	            this.container.on("click", selector, this.bind(function (e) {
	                if (!this.isInterfaceEnabled()) return;
	                if ($(e.target).closest(".select2-search-choice").length > 0) {
	                    // clicked inside a select2 search choice, do not open
	                    return;
	                }
	                this.selectChoice(null);
	                this.clearPlaceholder();
	                if (!this.container.hasClass("select2-container-active")) {
	                    this.opts.element.trigger($.Event("select2-focus"));
	                }
	                this.open();
	                this.focusSearch();
	                e.preventDefault();
	            }));

	            this.container.on("focus", selector, this.bind(function () {
	                if (!this.isInterfaceEnabled()) return;
	                if (!this.container.hasClass("select2-container-active")) {
	                    this.opts.element.trigger($.Event("select2-focus"));
	                }
	                this.container.addClass("select2-container-active");
	                this.dropdown.addClass("select2-drop-active");
	                this.clearPlaceholder();
	            }));

	            this.initContainerWidth();
	            this.opts.element.addClass("select2-offscreen");

	            // set the placeholder if necessary
	            this.clearSearch();
	        },

	        // multi
	        enableInterface: function() {
	            if (this.parent.enableInterface.apply(this, arguments)) {
	                this.search.prop("disabled", !this.isInterfaceEnabled());
	            }
	        },

	        // multi
	        initSelection: function () {
	            var data;
	            if (this.opts.element.val() === "" && this.opts.element.text() === "") {
	                this.updateSelection([]);
	                this.close();
	                // set the placeholder if necessary
	                this.clearSearch();
	            }
	            if (this.select || this.opts.element.val() !== "") {
	                var self = this;
	                this.opts.initSelection.call(null, this.opts.element, function(data){
	                    if (data !== undefined && data !== null) {
	                        self.updateSelection(data);
	                        self.close();
	                        // set the placeholder if necessary
	                        self.clearSearch();
	                    }
	                });
	            }
	        },

	        // multi
	        clearSearch: function () {
	            var placeholder = this.getPlaceholder(),
	                maxWidth = this.getMaxSearchWidth();

	            if (placeholder !== undefined  && this.getVal().length === 0 && this.search.hasClass("select2-focused") === false) {
	                this.search.val(placeholder).addClass("select2-default");
	                // stretch the search box to full width of the container so as much of the placeholder is visible as possible
	                // we could call this.resizeSearch(), but we do not because that requires a sizer and we do not want to create one so early because of a firefox bug, see #944
	                this.search.width(maxWidth > 0 ? maxWidth : this.container.css("width"));
	            } else {
	                this.search.val("").width(10);
	            }
	        },

	        // multi
	        clearPlaceholder: function () {
	            if (this.search.hasClass("select2-default")) {
	                this.search.val("").removeClass("select2-default");
	            }
	        },

	        // multi
	        opening: function () {
	            this.clearPlaceholder(); // should be done before super so placeholder is not used to search
	            this.resizeSearch();

	            this.parent.opening.apply(this, arguments);

	            this.focusSearch();

	            this.updateResults(true);
	            this.search.focus();
	            this.opts.element.trigger($.Event("select2-open"));
	        },

	        // multi
	        close: function () {
	            if (!this.opened()) return;
	            this.parent.close.apply(this, arguments);
	        },

	        // multi
	        focus: function () {
	            this.close();
	            this.search.focus();
	        },

	        // multi
	        isFocused: function () {
	            return this.search.hasClass("select2-focused");
	        },

	        // multi
	        updateSelection: function (data) {
	            var ids = [], filtered = [], self = this;

	            // filter out duplicates
	            $(data).each(function () {
	                if (indexOf(self.id(this), ids) < 0) {
	                    ids.push(self.id(this));
	                    filtered.push(this);
	                }
	            });
	            data = filtered;

	            this.selection.find(".select2-search-choice").remove();
	            $(data).each(function () {
	                self.addSelectedChoice(this);
	            });
	            self.postprocessResults();
	        },

	        // multi
	        tokenize: function() {
	            var input = this.search.val();
	            input = this.opts.tokenizer.call(this, input, this.data(), this.bind(this.onSelect), this.opts);
	            if (input != null && input != undefined) {
	                this.search.val(input);
	                if (input.length > 0) {
	                    this.open();
	                }
	            }

	        },

	        // multi
	        onSelect: function (data, options) {

	            if (!this.triggerSelect(data)) { return; }

	            this.addSelectedChoice(data);

	            this.opts.element.trigger({ type: "selected", val: this.id(data), choice: data });

	            if (this.select || !this.opts.closeOnSelect) this.postprocessResults(data, false, this.opts.closeOnSelect===true);

	            if (this.opts.closeOnSelect) {
	                this.close();
	                this.search.width(10);
	            } else {
	                if (this.countSelectableResults()>0) {
	                    this.search.width(10);
	                    this.resizeSearch();
	                    if (this.getMaximumSelectionSize() > 0 && this.val().length >= this.getMaximumSelectionSize()) {
	                        // if we reached max selection size repaint the results so choices
	                        // are replaced with the max selection reached message
	                        this.updateResults(true);
	                    }
	                    this.positionDropdown();
	                } else {
	                    // if nothing left to select close
	                    this.close();
	                    this.search.width(10);
	                }
	            }

	            // since its not possible to select an element that has already been
	            // added we do not need to check if this is a new element before firing change
	            this.triggerChange({ added: data });

	            if (!options || !options.noFocus)
	                this.focusSearch();
	        },

	        // multi
	        cancel: function () {
	            this.close();
	            this.focusSearch();
	        },

	        addSelectedChoice: function (data) {
	            var enableChoice = !data.locked,
	                enabledItem = $(
	                    "<li class='select2-search-choice'>" +
	                    "    <div></div>" +
	                    "    <a href='#' onclick='return false;' class='select2-search-choice-close' tabindex='-1'></a>" +
	                    "</li>"),
	                disabledItem = $(
	                    "<li class='select2-search-choice select2-locked'>" +
	                    "<div></div>" +
	                    "</li>");
	            var choice = enableChoice ? enabledItem : disabledItem,
	                id = this.id(data),
	                val = this.getVal(),
	                formatted,
	                cssClass;

	            formatted=this.opts.formatSelection(data, choice.find("div"), this.opts.escapeMarkup);
	            if (formatted != undefined) {
	                choice.find("div").replaceWith("<div>"+formatted+"</div>");
	            }
	            cssClass=this.opts.formatSelectionCssClass(data, choice.find("div"));
	            if (cssClass != undefined) {
	                choice.addClass(cssClass);
	            }

	            if(enableChoice){
	              choice.find(".select2-search-choice-close")
	                  .on("mousedown", killEvent)
	                  .on("click dblclick", this.bind(function (e) {
	                  if (!this.isInterfaceEnabled()) return;

	                  $(e.target).closest(".select2-search-choice").fadeOut('fast', this.bind(function(){
	                      this.unselect($(e.target));
	                      this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus");
	                      this.close();
	                      this.focusSearch();
	                  })).dequeue();
	                  killEvent(e);
	              })).on("focus", this.bind(function () {
	                  if (!this.isInterfaceEnabled()) return;
	                  this.container.addClass("select2-container-active");
	                  this.dropdown.addClass("select2-drop-active");
	              }));
	            }

	            choice.data("select2-data", data);
	            choice.insertBefore(this.searchContainer);

	            val.push(id);
	            this.setVal(val);
	        },

	        // multi
	        unselect: function (selected) {
	            var val = this.getVal(),
	                data,
	                index;
	            selected = selected.closest(".select2-search-choice");

	            if (selected.length === 0) {
	                throw "Invalid argument: " + selected + ". Must be .select2-search-choice";
	            }

	            data = selected.data("select2-data");

	            if (!data) {
	                // prevent a race condition when the 'x' is clicked really fast repeatedly the event can be queued
	                // and invoked on an element already removed
	                return;
	            }

	            while((index = indexOf(this.id(data), val)) >= 0) {
	                val.splice(index, 1);
	                this.setVal(val);
	                if (this.select) this.postprocessResults();
	            }

	            var evt = $.Event("select2-removing");
	            evt.val = this.id(data);
	            evt.choice = data;
	            this.opts.element.trigger(evt);

	            if (evt.isDefaultPrevented()) {
	                return;
	            }

	            selected.remove();

	            this.opts.element.trigger({ type: "select2-removed", val: this.id(data), choice: data });
	            this.triggerChange({ removed: data });
	        },

	        // multi
	        postprocessResults: function (data, initial, noHighlightUpdate) {
	            var val = this.getVal(),
	                choices = this.results.find(".select2-result"),
	                compound = this.results.find(".select2-result-with-children"),
	                self = this;

	            choices.each2(function (i, choice) {
	                var id = self.id(choice.data("select2-data"));
	                if (indexOf(id, val) >= 0) {
	                    choice.addClass("select2-selected");
	                    // mark all children of the selected parent as selected
	                    choice.find(".select2-result-selectable").addClass("select2-selected");
	                }
	            });

	            compound.each2(function(i, choice) {
	                // hide an optgroup if it doesnt have any selectable children
	                if (!choice.is('.select2-result-selectable')
	                    && choice.find(".select2-result-selectable:not(.select2-selected)").length === 0) {
	                    choice.addClass("select2-selected");
	                }
	            });

	            if (this.highlight() == -1 && noHighlightUpdate !== false){
	                self.highlight(0);
	            }

	            //If all results are chosen render formatNoMAtches
	            if(!this.opts.createSearchChoice && !choices.filter('.select2-result:not(.select2-selected)').length > 0){
	                if(!data || data && !data.more && this.results.find(".select2-no-results").length === 0) {
	                    if (checkFormatter(self.opts.formatNoMatches, "formatNoMatches")) {
	                        this.results.append("<li class='select2-no-results'>" + self.opts.formatNoMatches(self.search.val()) + "</li>");
	                    }
	                }
	            }

	        },

	        // multi
	        getMaxSearchWidth: function() {
	            return this.selection.width() - getSideBorderPadding(this.search);
	        },

	        // multi
	        resizeSearch: function () {
	            var minimumWidth, left, maxWidth, containerLeft, searchWidth,
	                sideBorderPadding = getSideBorderPadding(this.search);

	            minimumWidth = measureTextWidth(this.search) + 10;

	            left = this.search.offset().left;

	            maxWidth = this.selection.width();
	            containerLeft = this.selection.offset().left;

	            searchWidth = maxWidth - (left - containerLeft) - sideBorderPadding;

	            if (searchWidth < minimumWidth) {
	                searchWidth = maxWidth - sideBorderPadding;
	            }

	            if (searchWidth < 40) {
	                searchWidth = maxWidth - sideBorderPadding;
	            }

	            if (searchWidth <= 0) {
	              searchWidth = minimumWidth;
	            }

	            this.search.width(Math.floor(searchWidth));
	        },

	        // multi
	        getVal: function () {
	            var val;
	            if (this.select) {
	                val = this.select.val();
	                return val === null ? [] : val;
	            } else {
	                val = this.opts.element.val();
	                return splitVal(val, this.opts.separator);
	            }
	        },

	        // multi
	        setVal: function (val) {
	            var unique;
	            if (this.select) {
	                this.select.val(val);
	            } else {
	                unique = [];
	                // filter out duplicates
	                $(val).each(function () {
	                    if (indexOf(this, unique) < 0) unique.push(this);
	                });
	                this.opts.element.val(unique.length === 0 ? "" : unique.join(this.opts.separator));
	            }
	        },

	        // multi
	        buildChangeDetails: function (old, current) {
	            var current = current.slice(0),
	                old = old.slice(0);

	            // remove intersection from each array
	            for (var i = 0; i < current.length; i++) {
	                for (var j = 0; j < old.length; j++) {
	                    if (equal(this.opts.id(current[i]), this.opts.id(old[j]))) {
	                        current.splice(i, 1);
	                        if(i>0){
	                        	i--;
	                        }
	                        old.splice(j, 1);
	                        j--;
	                    }
	                }
	            }

	            return {added: current, removed: old};
	        },


	        // multi
	        val: function (val, triggerChange) {
	            var oldData, self=this;

	            if (arguments.length === 0) {
	                return this.getVal();
	            }

	            oldData=this.data();
	            if (!oldData.length) oldData=[];

	            // val is an id. !val is true for [undefined,null,'',0] - 0 is legal
	            if (!val && val !== 0) {
	                this.opts.element.val("");
	                this.updateSelection([]);
	                this.clearSearch();
	                if (triggerChange) {
	                    this.triggerChange({added: this.data(), removed: oldData});
	                }
	                return;
	            }

	            // val is a list of ids
	            this.setVal(val);

	            if (this.select) {
	                this.opts.initSelection(this.select, this.bind(this.updateSelection));
	                if (triggerChange) {
	                    this.triggerChange(this.buildChangeDetails(oldData, this.data()));
	                }
	            } else {
	                if (this.opts.initSelection === undefined) {
	                    throw new Error("val() cannot be called if initSelection() is not defined");
	                }

	                this.opts.initSelection(this.opts.element, function(data){
	                    var ids=$.map(data, self.id);
	                    self.setVal(ids);
	                    self.updateSelection(data);
	                    self.clearSearch();
	                    if (triggerChange) {
	                        self.triggerChange(self.buildChangeDetails(oldData, self.data()));
	                    }
	                });
	            }
	            this.clearSearch();
	        },

	        // multi
	        onSortStart: function() {
	            if (this.select) {
	                throw new Error("Sorting of elements is not supported when attached to <select>. Attach to <input type='hidden'/> instead.");
	            }

	            // collapse search field into 0 width so its container can be collapsed as well
	            this.search.width(0);
	            // hide the container
	            this.searchContainer.hide();
	        },

	        // multi
	        onSortEnd:function() {

	            var val=[], self=this;

	            // show search and move it to the end of the list
	            this.searchContainer.show();
	            // make sure the search container is the last item in the list
	            this.searchContainer.appendTo(this.searchContainer.parent());
	            // since we collapsed the width in dragStarted, we resize it here
	            this.resizeSearch();

	            // update selection
	            this.selection.find(".select2-search-choice").each(function() {
	                val.push(self.opts.id($(this).data("select2-data")));
	            });
	            this.setVal(val);
	            this.triggerChange();
	        },

	        // multi
	        data: function(values, triggerChange) {
	            var self=this, ids, old;
	            if (arguments.length === 0) {
	                 return this.selection
	                     .find(".select2-search-choice")
	                     .map(function() { return $(this).data("select2-data"); })
	                     .get();
	            } else {
	                old = this.data();
	                if (!values) { values = []; }
	                ids = $.map(values, function(e) { return self.opts.id(e); });
	                this.setVal(ids);
	                this.updateSelection(values);
	                this.clearSearch();
	                if (triggerChange) {
	                    this.triggerChange(this.buildChangeDetails(old, this.data()));
	                }
	            }
	        }
	    });

	    $.fn.select2 = function () {

	        var args = Array.prototype.slice.call(arguments, 0),
	            opts,
	            select2,
	            method, value, multiple,
	            allowedMethods = ["val", "destroy", "opened", "open", "close", "focus", "isFocused", "container", "dropdown", "onSortStart", "onSortEnd", "enable", "disable", "readonly", "positionDropdown", "data", "search"],
	            valueMethods = ["opened", "isFocused", "container", "dropdown"],
	            propertyMethods = ["val", "data"],
	            methodsMap = { search: "externalSearch" };

	        this.each(function () {
	            if (args.length === 0 || typeof(args[0]) === "object") {
	                opts = args.length === 0 ? {} : $.extend({}, args[0]);
	                opts.element = $(this);

	                if (opts.element.get(0).tagName.toLowerCase() === "select") {
	                    multiple = opts.element.prop("multiple");
	                } else {
	                    multiple = opts.multiple || false;
	                    if ("tags" in opts) {opts.multiple = multiple = true;}
	                }

	                select2 = multiple ? new MultiSelect2() : new SingleSelect2();
	                select2.init(opts);
	            } else if (typeof(args[0]) === "string") {

	                if (indexOf(args[0], allowedMethods) < 0) {
	                    throw "Unknown method: " + args[0];
	                }

	                value = undefined;
	                select2 = $(this).data("select2");
	                if (select2 === undefined) return;

	                method=args[0];

	                if (method === "container") {
	                    value = select2.container;
	                } else if (method === "dropdown") {
	                    value = select2.dropdown;
	                } else {
	                    if (methodsMap[method]) method = methodsMap[method];

	                    value = select2[method].apply(select2, args.slice(1));
	                }
	                if (indexOf(args[0], valueMethods) >= 0
	                    || (indexOf(args[0], propertyMethods) && args.length == 1)) {
	                    return false; // abort the iteration, ready to return first matched value
	                }
	            } else {
	                throw "Invalid arguments to select2 plugin: " + args;
	            }
	        });
	        return (value === undefined) ? this : value;
	    };

	    // plugin defaults, accessible to users
	    $.fn.select2.defaults = {
	        width: "copy",
	        loadMorePadding: 0,
	        closeOnSelect: true,
	        openOnEnter: true,
	        containerCss: {},
	        dropdownCss: {},
	        containerCssClass: "",
	        dropdownCssClass: "",
	        formatResult: function(result, container, query, escapeMarkup) {
	            var markup=[];
	            markMatch(result.text, query.term, markup, escapeMarkup);
	            return markup.join("");
	        },
	        formatSelection: function (data, container, escapeMarkup) {
	            return data ? escapeMarkup(data.text) : undefined;
	        },
	        sortResults: function (results, container, query) {
	            return results;
	        },
	        formatResultCssClass: function(data) {return undefined;},
	        formatSelectionCssClass: function(data, container) {return undefined;},
	        formatNoMatches: function () { return "No matches found"; },
	        formatInputTooShort: function (input, min) { var n = min - input.length; return "Please enter " + n + " more character" + (n == 1? "" : "s"); },
	        formatInputTooLong: function (input, max) { var n = input.length - max; return "Please delete " + n + " character" + (n == 1? "" : "s"); },
	        formatSelectionTooBig: function (limit) { return "You can only select " + limit + " item" + (limit == 1 ? "" : "s"); },
	        formatLoadMore: function (pageNumber) { return "Loading more results..."; },
	        formatSearching: function () { return "Searching..."; },
	        minimumResultsForSearch: 0,
	        minimumInputLength: 0,
	        maximumInputLength: null,
	        maximumSelectionSize: 0,
	        id: function (e) { return e.id; },
	        matcher: function(term, text) {
	            return stripDiacritics(''+text).toUpperCase().indexOf(stripDiacritics(''+term).toUpperCase()) >= 0;
	        },
	        separator: ",",
	        tokenSeparators: [],
	        tokenizer: defaultTokenizer,
	        escapeMarkup: defaultEscapeMarkup,
	        blurOnChange: false,
	        selectOnBlur: false,
	        adaptContainerCssClass: function(c) { return c; },
	        adaptDropdownCssClass: function(c) { return null; },
	        nextSearchTerm: function(selectedObject, currentSearchTerm) { return undefined; }
	    };

	    $.fn.select2.ajaxDefaults = {
	        transport: $.ajax,
	        params: {
	            type: "GET",
	            cache: false,
	            dataType: "json"
	        }
	    };

	    // exports
	    window.Select2 = {
	        query: {
	            ajax: ajax,
	            local: local,
	            tags: tags
	        }, util: {
	            debounce: debounce,
	            markMatch: markMatch,
	            escapeMarkup: defaultEscapeMarkup,
	            stripDiacritics: stripDiacritics
	        }, "class": {
	            "abstract": AbstractSelect2,
	            "single": SingleSelect2,
	            "multi": MultiSelect2
	        }
	    };

	}(jQuery));
	;
	/*!
	 * jQuery UI Core 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/category/ui-core/
	 */
	(function( $, undefined ) {

	var uuid = 0,
		runiqueId = /^ui-id-\d+$/;

	// $.ui might exist from components with no dependencies, e.g., $.ui.position
	$.ui = $.ui || {};

	$.extend( $.ui, {
		version: "1.10.4",

		keyCode: {
			BACKSPACE: 8,
			COMMA: 188,
			DELETE: 46,
			DOWN: 40,
			END: 35,
			ENTER: 13,
			ESCAPE: 27,
			HOME: 36,
			LEFT: 37,
			NUMPAD_ADD: 107,
			NUMPAD_DECIMAL: 110,
			NUMPAD_DIVIDE: 111,
			NUMPAD_ENTER: 108,
			NUMPAD_MULTIPLY: 106,
			NUMPAD_SUBTRACT: 109,
			PAGE_DOWN: 34,
			PAGE_UP: 33,
			PERIOD: 190,
			RIGHT: 39,
			SPACE: 32,
			TAB: 9,
			UP: 38
		}
	});

	// plugins
	$.fn.extend({
		focus: (function( orig ) {
			return function( delay, fn ) {
				return typeof delay === "number" ?
					this.each(function() {
						var elem = this;
						setTimeout(function() {
							$( elem ).focus();
							if ( fn ) {
								fn.call( elem );
							}
						}, delay );
					}) :
					orig.apply( this, arguments );
			};
		})( $.fn.focus ),

		scrollParent: function() {
			var scrollParent;
			if (($.ui.ie && (/(static|relative)/).test(this.css("position"))) || (/absolute/).test(this.css("position"))) {
				scrollParent = this.parents().filter(function() {
					return (/(relative|absolute|fixed)/).test($.css(this,"position")) && (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
				}).eq(0);
			} else {
				scrollParent = this.parents().filter(function() {
					return (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
				}).eq(0);
			}

			return (/fixed/).test(this.css("position")) || !scrollParent.length ? $(document) : scrollParent;
		},

		zIndex: function( zIndex ) {
			if ( zIndex !== undefined ) {
				return this.css( "zIndex", zIndex );
			}

			if ( this.length ) {
				var elem = $( this[ 0 ] ), position, value;
				while ( elem.length && elem[ 0 ] !== document ) {
					// Ignore z-index if position is set to a value where z-index is ignored by the browser
					// This makes behavior of this function consistent across browsers
					// WebKit always returns auto if the element is positioned
					position = elem.css( "position" );
					if ( position === "absolute" || position === "relative" || position === "fixed" ) {
						// IE returns 0 when zIndex is not specified
						// other browsers return a string
						// we ignore the case of nested elements with an explicit value of 0
						// <div style="z-index: -10;"><div style="z-index: 0;"></div></div>
						value = parseInt( elem.css( "zIndex" ), 10 );
						if ( !isNaN( value ) && value !== 0 ) {
							return value;
						}
					}
					elem = elem.parent();
				}
			}

			return 0;
		},

		uniqueId: function() {
			return this.each(function() {
				if ( !this.id ) {
					this.id = "ui-id-" + (++uuid);
				}
			});
		},

		removeUniqueId: function() {
			return this.each(function() {
				if ( runiqueId.test( this.id ) ) {
					$( this ).removeAttr( "id" );
				}
			});
		}
	});

	// selectors
	function focusable( element, isTabIndexNotNaN ) {
		var map, mapName, img,
			nodeName = element.nodeName.toLowerCase();
		if ( "area" === nodeName ) {
			map = element.parentNode;
			mapName = map.name;
			if ( !element.href || !mapName || map.nodeName.toLowerCase() !== "map" ) {
				return false;
			}
			img = $( "img[usemap=#" + mapName + "]" )[0];
			return !!img && visible( img );
		}
		return ( /input|select|textarea|button|object/.test( nodeName ) ?
			!element.disabled :
			"a" === nodeName ?
				element.href || isTabIndexNotNaN :
				isTabIndexNotNaN) &&
			// the element and all of its ancestors must be visible
			visible( element );
	}

	function visible( element ) {
		return $.expr.filters.visible( element ) &&
			!$( element ).parents().addBack().filter(function() {
				return $.css( this, "visibility" ) === "hidden";
			}).length;
	}

	$.extend( $.expr[ ":" ], {
		data: $.expr.createPseudo ?
			$.expr.createPseudo(function( dataName ) {
				return function( elem ) {
					return !!$.data( elem, dataName );
				};
			}) :
			// support: jQuery <1.8
			function( elem, i, match ) {
				return !!$.data( elem, match[ 3 ] );
			},

		focusable: function( element ) {
			return focusable( element, !isNaN( $.attr( element, "tabindex" ) ) );
		},

		tabbable: function( element ) {
			var tabIndex = $.attr( element, "tabindex" ),
				isTabIndexNaN = isNaN( tabIndex );
			return ( isTabIndexNaN || tabIndex >= 0 ) && focusable( element, !isTabIndexNaN );
		}
	});

	// support: jQuery <1.8
	if ( !$( "<a>" ).outerWidth( 1 ).jquery ) {
		$.each( [ "Width", "Height" ], function( i, name ) {
			var side = name === "Width" ? [ "Left", "Right" ] : [ "Top", "Bottom" ],
				type = name.toLowerCase(),
				orig = {
					innerWidth: $.fn.innerWidth,
					innerHeight: $.fn.innerHeight,
					outerWidth: $.fn.outerWidth,
					outerHeight: $.fn.outerHeight
				};

			function reduce( elem, size, border, margin ) {
				$.each( side, function() {
					size -= parseFloat( $.css( elem, "padding" + this ) ) || 0;
					if ( border ) {
						size -= parseFloat( $.css( elem, "border" + this + "Width" ) ) || 0;
					}
					if ( margin ) {
						size -= parseFloat( $.css( elem, "margin" + this ) ) || 0;
					}
				});
				return size;
			}

			$.fn[ "inner" + name ] = function( size ) {
				if ( size === undefined ) {
					return orig[ "inner" + name ].call( this );
				}

				return this.each(function() {
					$( this ).css( type, reduce( this, size ) + "px" );
				});
			};

			$.fn[ "outer" + name] = function( size, margin ) {
				if ( typeof size !== "number" ) {
					return orig[ "outer" + name ].call( this, size );
				}

				return this.each(function() {
					$( this).css( type, reduce( this, size, true, margin ) + "px" );
				});
			};
		});
	}

	// support: jQuery <1.8
	if ( !$.fn.addBack ) {
		$.fn.addBack = function( selector ) {
			return this.add( selector == null ?
				this.prevObject : this.prevObject.filter( selector )
			);
		};
	}

	// support: jQuery 1.6.1, 1.6.2 (http://bugs.jquery.com/ticket/9413)
	if ( $( "<a>" ).data( "a-b", "a" ).removeData( "a-b" ).data( "a-b" ) ) {
		$.fn.removeData = (function( removeData ) {
			return function( key ) {
				if ( arguments.length ) {
					return removeData.call( this, $.camelCase( key ) );
				} else {
					return removeData.call( this );
				}
			};
		})( $.fn.removeData );
	}





	// deprecated
	$.ui.ie = !!/msie [\w.]+/.exec( navigator.userAgent.toLowerCase() );

	$.support.selectstart = "onselectstart" in document.createElement( "div" );
	$.fn.extend({
		disableSelection: function() {
			return this.bind( ( $.support.selectstart ? "selectstart" : "mousedown" ) +
				".ui-disableSelection", function( event ) {
					event.preventDefault();
				});
		},

		enableSelection: function() {
			return this.unbind( ".ui-disableSelection" );
		}
	});

	$.extend( $.ui, {
		// $.ui.plugin is deprecated. Use $.widget() extensions instead.
		plugin: {
			add: function( module, option, set ) {
				var i,
					proto = $.ui[ module ].prototype;
				for ( i in set ) {
					proto.plugins[ i ] = proto.plugins[ i ] || [];
					proto.plugins[ i ].push( [ option, set[ i ] ] );
				}
			},
			call: function( instance, name, args ) {
				var i,
					set = instance.plugins[ name ];
				if ( !set || !instance.element[ 0 ].parentNode || instance.element[ 0 ].parentNode.nodeType === 11 ) {
					return;
				}

				for ( i = 0; i < set.length; i++ ) {
					if ( instance.options[ set[ i ][ 0 ] ] ) {
						set[ i ][ 1 ].apply( instance.element, args );
					}
				}
			}
		},

		// only used by resizable
		hasScroll: function( el, a ) {

			//If overflow is hidden, the element might have extra content, but the user wants to hide it
			if ( $( el ).css( "overflow" ) === "hidden") {
				return false;
			}

			var scroll = ( a && a === "left" ) ? "scrollLeft" : "scrollTop",
				has = false;

			if ( el[ scroll ] > 0 ) {
				return true;
			}

			// TODO: determine which cases actually cause this to happen
			// if the element doesn't have the scroll set, see if it's possible to
			// set the scroll
			el[ scroll ] = 1;
			has = ( el[ scroll ] > 0 );
			el[ scroll ] = 0;
			return has;
		}
	});

	})( jQuery );
	;
	/*!
	 * jQuery UI Widget 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/jQuery.widget/
	 */
	(function( $, undefined ) {

	var uuid = 0,
		slice = Array.prototype.slice,
		_cleanData = $.cleanData;
	$.cleanData = function( elems ) {
		for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
			try {
				$( elem ).triggerHandler( "remove" );
			// http://bugs.jquery.com/ticket/8235
			} catch( e ) {}
		}
		_cleanData( elems );
	};

	$.widget = function( name, base, prototype ) {
		var fullName, existingConstructor, constructor, basePrototype,
			// proxiedPrototype allows the provided prototype to remain unmodified
			// so that it can be used as a mixin for multiple widgets (#8876)
			proxiedPrototype = {},
			namespace = name.split( "." )[ 0 ];

		name = name.split( "." )[ 1 ];
		fullName = namespace + "-" + name;

		if ( !prototype ) {
			prototype = base;
			base = $.Widget;
		}

		// create selector for plugin
		$.expr[ ":" ][ fullName.toLowerCase() ] = function( elem ) {
			return !!$.data( elem, fullName );
		};

		$[ namespace ] = $[ namespace ] || {};
		existingConstructor = $[ namespace ][ name ];
		constructor = $[ namespace ][ name ] = function( options, element ) {
			// allow instantiation without "new" keyword
			if ( !this._createWidget ) {
				return new constructor( options, element );
			}

			// allow instantiation without initializing for simple inheritance
			// must use "new" keyword (the code above always passes args)
			if ( arguments.length ) {
				this._createWidget( options, element );
			}
		};
		// extend with the existing constructor to carry over any static properties
		$.extend( constructor, existingConstructor, {
			version: prototype.version,
			// copy the object used to create the prototype in case we need to
			// redefine the widget later
			_proto: $.extend( {}, prototype ),
			// track widgets that inherit from this widget in case this widget is
			// redefined after a widget inherits from it
			_childConstructors: []
		});

		basePrototype = new base();
		// we need to make the options hash a property directly on the new instance
		// otherwise we'll modify the options hash on the prototype that we're
		// inheriting from
		basePrototype.options = $.widget.extend( {}, basePrototype.options );
		$.each( prototype, function( prop, value ) {
			if ( !$.isFunction( value ) ) {
				proxiedPrototype[ prop ] = value;
				return;
			}
			proxiedPrototype[ prop ] = (function() {
				var _super = function() {
						return base.prototype[ prop ].apply( this, arguments );
					},
					_superApply = function( args ) {
						return base.prototype[ prop ].apply( this, args );
					};
				return function() {
					var __super = this._super,
						__superApply = this._superApply,
						returnValue;

					this._super = _super;
					this._superApply = _superApply;

					returnValue = value.apply( this, arguments );

					this._super = __super;
					this._superApply = __superApply;

					return returnValue;
				};
			})();
		});
		constructor.prototype = $.widget.extend( basePrototype, {
			// TODO: remove support for widgetEventPrefix
			// always use the name + a colon as the prefix, e.g., draggable:start
			// don't prefix for widgets that aren't DOM-based
			widgetEventPrefix: existingConstructor ? (basePrototype.widgetEventPrefix || name) : name
		}, proxiedPrototype, {
			constructor: constructor,
			namespace: namespace,
			widgetName: name,
			widgetFullName: fullName
		});

		// If this widget is being redefined then we need to find all widgets that
		// are inheriting from it and redefine all of them so that they inherit from
		// the new version of this widget. We're essentially trying to replace one
		// level in the prototype chain.
		if ( existingConstructor ) {
			$.each( existingConstructor._childConstructors, function( i, child ) {
				var childPrototype = child.prototype;

				// redefine the child widget using the same prototype that was
				// originally used, but inherit from the new version of the base
				$.widget( childPrototype.namespace + "." + childPrototype.widgetName, constructor, child._proto );
			});
			// remove the list of existing child constructors from the old constructor
			// so the old child constructors can be garbage collected
			delete existingConstructor._childConstructors;
		} else {
			base._childConstructors.push( constructor );
		}

		$.widget.bridge( name, constructor );
	};

	$.widget.extend = function( target ) {
		var input = slice.call( arguments, 1 ),
			inputIndex = 0,
			inputLength = input.length,
			key,
			value;
		for ( ; inputIndex < inputLength; inputIndex++ ) {
			for ( key in input[ inputIndex ] ) {
				value = input[ inputIndex ][ key ];
				if ( input[ inputIndex ].hasOwnProperty( key ) && value !== undefined ) {
					// Clone objects
					if ( $.isPlainObject( value ) ) {
						target[ key ] = $.isPlainObject( target[ key ] ) ?
							$.widget.extend( {}, target[ key ], value ) :
							// Don't extend strings, arrays, etc. with objects
							$.widget.extend( {}, value );
					// Copy everything else by reference
					} else {
						target[ key ] = value;
					}
				}
			}
		}
		return target;
	};

	$.widget.bridge = function( name, object ) {
		var fullName = object.prototype.widgetFullName || name;
		$.fn[ name ] = function( options ) {
			var isMethodCall = typeof options === "string",
				args = slice.call( arguments, 1 ),
				returnValue = this;

			// allow multiple hashes to be passed on init
			options = !isMethodCall && args.length ?
				$.widget.extend.apply( null, [ options ].concat(args) ) :
				options;

			if ( isMethodCall ) {
				this.each(function() {
					var methodValue,
						instance = $.data( this, fullName );
					if ( !instance ) {
						return $.error( "cannot call methods on " + name + " prior to initialization; " +
							"attempted to call method '" + options + "'" );
					}
					if ( !$.isFunction( instance[options] ) || options.charAt( 0 ) === "_" ) {
						return $.error( "no such method '" + options + "' for " + name + " widget instance" );
					}
					methodValue = instance[ options ].apply( instance, args );
					if ( methodValue !== instance && methodValue !== undefined ) {
						returnValue = methodValue && methodValue.jquery ?
							returnValue.pushStack( methodValue.get() ) :
							methodValue;
						return false;
					}
				});
			} else {
				this.each(function() {
					var instance = $.data( this, fullName );
					if ( instance ) {
						instance.option( options || {} )._init();
					} else {
						$.data( this, fullName, new object( options, this ) );
					}
				});
			}

			return returnValue;
		};
	};

	$.Widget = function( /* options, element */ ) {};
	$.Widget._childConstructors = [];

	$.Widget.prototype = {
		widgetName: "widget",
		widgetEventPrefix: "",
		defaultElement: "<div>",
		options: {
			disabled: false,

			// callbacks
			create: null
		},
		_createWidget: function( options, element ) {
			element = $( element || this.defaultElement || this )[ 0 ];
			this.element = $( element );
			this.uuid = uuid++;
			this.eventNamespace = "." + this.widgetName + this.uuid;
			this.options = $.widget.extend( {},
				this.options,
				this._getCreateOptions(),
				options );

			this.bindings = $();
			this.hoverable = $();
			this.focusable = $();

			if ( element !== this ) {
				$.data( element, this.widgetFullName, this );
				this._on( true, this.element, {
					remove: function( event ) {
						if ( event.target === element ) {
							this.destroy();
						}
					}
				});
				this.document = $( element.style ?
					// element within the document
					element.ownerDocument :
					// element is window or document
					element.document || element );
				this.window = $( this.document[0].defaultView || this.document[0].parentWindow );
			}

			this._create();
			this._trigger( "create", null, this._getCreateEventData() );
			this._init();
		},
		_getCreateOptions: $.noop,
		_getCreateEventData: $.noop,
		_create: $.noop,
		_init: $.noop,

		destroy: function() {
			this._destroy();
			// we can probably remove the unbind calls in 2.0
			// all event bindings should go through this._on()
			this.element
				.unbind( this.eventNamespace )
				// 1.9 BC for #7810
				// TODO remove dual storage
				.removeData( this.widgetName )
				.removeData( this.widgetFullName )
				// support: jquery <1.6.3
				// http://bugs.jquery.com/ticket/9413
				.removeData( $.camelCase( this.widgetFullName ) );
			this.widget()
				.unbind( this.eventNamespace )
				.removeAttr( "aria-disabled" )
				.removeClass(
					this.widgetFullName + "-disabled " +
					"ui-state-disabled" );

			// clean up events and states
			this.bindings.unbind( this.eventNamespace );
			this.hoverable.removeClass( "ui-state-hover" );
			this.focusable.removeClass( "ui-state-focus" );
		},
		_destroy: $.noop,

		widget: function() {
			return this.element;
		},

		option: function( key, value ) {
			var options = key,
				parts,
				curOption,
				i;

			if ( arguments.length === 0 ) {
				// don't return a reference to the internal hash
				return $.widget.extend( {}, this.options );
			}

			if ( typeof key === "string" ) {
				// handle nested keys, e.g., "foo.bar" => { foo: { bar: ___ } }
				options = {};
				parts = key.split( "." );
				key = parts.shift();
				if ( parts.length ) {
					curOption = options[ key ] = $.widget.extend( {}, this.options[ key ] );
					for ( i = 0; i < parts.length - 1; i++ ) {
						curOption[ parts[ i ] ] = curOption[ parts[ i ] ] || {};
						curOption = curOption[ parts[ i ] ];
					}
					key = parts.pop();
					if ( arguments.length === 1 ) {
						return curOption[ key ] === undefined ? null : curOption[ key ];
					}
					curOption[ key ] = value;
				} else {
					if ( arguments.length === 1 ) {
						return this.options[ key ] === undefined ? null : this.options[ key ];
					}
					options[ key ] = value;
				}
			}

			this._setOptions( options );

			return this;
		},
		_setOptions: function( options ) {
			var key;

			for ( key in options ) {
				this._setOption( key, options[ key ] );
			}

			return this;
		},
		_setOption: function( key, value ) {
			this.options[ key ] = value;

			if ( key === "disabled" ) {
				this.widget()
					.toggleClass( this.widgetFullName + "-disabled ui-state-disabled", !!value )
					.attr( "aria-disabled", value );
				this.hoverable.removeClass( "ui-state-hover" );
				this.focusable.removeClass( "ui-state-focus" );
			}

			return this;
		},

		enable: function() {
			return this._setOption( "disabled", false );
		},
		disable: function() {
			return this._setOption( "disabled", true );
		},

		_on: function( suppressDisabledCheck, element, handlers ) {
			var delegateElement,
				instance = this;

			// no suppressDisabledCheck flag, shuffle arguments
			if ( typeof suppressDisabledCheck !== "boolean" ) {
				handlers = element;
				element = suppressDisabledCheck;
				suppressDisabledCheck = false;
			}

			// no element argument, shuffle and use this.element
			if ( !handlers ) {
				handlers = element;
				element = this.element;
				delegateElement = this.widget();
			} else {
				// accept selectors, DOM elements
				element = delegateElement = $( element );
				this.bindings = this.bindings.add( element );
			}

			$.each( handlers, function( event, handler ) {
				function handlerProxy() {
					// allow widgets to customize the disabled handling
					// - disabled as an array instead of boolean
					// - disabled class as method for disabling individual parts
					if ( !suppressDisabledCheck &&
							( instance.options.disabled === true ||
								$( this ).hasClass( "ui-state-disabled" ) ) ) {
						return;
					}
					return ( typeof handler === "string" ? instance[ handler ] : handler )
						.apply( instance, arguments );
				}

				// copy the guid so direct unbinding works
				if ( typeof handler !== "string" ) {
					handlerProxy.guid = handler.guid =
						handler.guid || handlerProxy.guid || $.guid++;
				}

				var match = event.match( /^(\w+)\s*(.*)$/ ),
					eventName = match[1] + instance.eventNamespace,
					selector = match[2];
				if ( selector ) {
					delegateElement.delegate( selector, eventName, handlerProxy );
				} else {
					element.bind( eventName, handlerProxy );
				}
			});
		},

		_off: function( element, eventName ) {
			eventName = (eventName || "").split( " " ).join( this.eventNamespace + " " ) + this.eventNamespace;
			element.unbind( eventName ).undelegate( eventName );
		},

		_delay: function( handler, delay ) {
			function handlerProxy() {
				return ( typeof handler === "string" ? instance[ handler ] : handler )
					.apply( instance, arguments );
			}
			var instance = this;
			return setTimeout( handlerProxy, delay || 0 );
		},

		_hoverable: function( element ) {
			this.hoverable = this.hoverable.add( element );
			this._on( element, {
				mouseenter: function( event ) {
					$( event.currentTarget ).addClass( "ui-state-hover" );
				},
				mouseleave: function( event ) {
					$( event.currentTarget ).removeClass( "ui-state-hover" );
				}
			});
		},

		_focusable: function( element ) {
			this.focusable = this.focusable.add( element );
			this._on( element, {
				focusin: function( event ) {
					$( event.currentTarget ).addClass( "ui-state-focus" );
				},
				focusout: function( event ) {
					$( event.currentTarget ).removeClass( "ui-state-focus" );
				}
			});
		},

		_trigger: function( type, event, data ) {
			var prop, orig,
				callback = this.options[ type ];

			data = data || {};
			event = $.Event( event );
			event.type = ( type === this.widgetEventPrefix ?
				type :
				this.widgetEventPrefix + type ).toLowerCase();
			// the original event may come from any element
			// so we need to reset the target on the new event
			event.target = this.element[ 0 ];

			// copy original event properties over to the new event
			orig = event.originalEvent;
			if ( orig ) {
				for ( prop in orig ) {
					if ( !( prop in event ) ) {
						event[ prop ] = orig[ prop ];
					}
				}
			}

			this.element.trigger( event, data );
			return !( $.isFunction( callback ) &&
				callback.apply( this.element[0], [ event ].concat( data ) ) === false ||
				event.isDefaultPrevented() );
		}
	};

	$.each( { show: "fadeIn", hide: "fadeOut" }, function( method, defaultEffect ) {
		$.Widget.prototype[ "_" + method ] = function( element, options, callback ) {
			if ( typeof options === "string" ) {
				options = { effect: options };
			}
			var hasOptions,
				effectName = !options ?
					method :
					options === true || typeof options === "number" ?
						defaultEffect :
						options.effect || defaultEffect;
			options = options || {};
			if ( typeof options === "number" ) {
				options = { duration: options };
			}
			hasOptions = !$.isEmptyObject( options );
			options.complete = callback;
			if ( options.delay ) {
				element.delay( options.delay );
			}
			if ( hasOptions && $.effects && $.effects.effect[ effectName ] ) {
				element[ method ]( options );
			} else if ( effectName !== method && element[ effectName ] ) {
				element[ effectName ]( options.duration, options.easing, callback );
			} else {
				element.queue(function( next ) {
					$( this )[ method ]();
					if ( callback ) {
						callback.call( element[ 0 ] );
					}
					next();
				});
			}
		};
	});

	})( jQuery );
	;
	/*!
	 * jQuery UI Mouse 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/mouse/
	 *
	 * Depends:
	 *	jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	var mouseHandled = false;
	$( document ).mouseup( function() {
		mouseHandled = false;
	});

	$.widget("ui.mouse", {
		version: "1.10.4",
		options: {
			cancel: "input,textarea,button,select,option",
			distance: 1,
			delay: 0
		},
		_mouseInit: function() {
			var that = this;

			this.element
				.bind("mousedown."+this.widgetName, function(event) {
					return that._mouseDown(event);
				})
				.bind("click."+this.widgetName, function(event) {
					if (true === $.data(event.target, that.widgetName + ".preventClickEvent")) {
						$.removeData(event.target, that.widgetName + ".preventClickEvent");
						event.stopImmediatePropagation();
						return false;
					}
				});

			this.started = false;
		},

		// TODO: make sure destroying one instance of mouse doesn't mess with
		// other instances of mouse
		_mouseDestroy: function() {
			this.element.unbind("."+this.widgetName);
			if ( this._mouseMoveDelegate ) {
				$(document)
					.unbind("mousemove."+this.widgetName, this._mouseMoveDelegate)
					.unbind("mouseup."+this.widgetName, this._mouseUpDelegate);
			}
		},

		_mouseDown: function(event) {
			// don't let more than one widget handle mouseStart
			if( mouseHandled ) { return; }

			// we may have missed mouseup (out of window)
			(this._mouseStarted && this._mouseUp(event));

			this._mouseDownEvent = event;

			var that = this,
				btnIsLeft = (event.which === 1),
				// event.target.nodeName works around a bug in IE 8 with
				// disabled inputs (#7620)
				elIsCancel = (typeof this.options.cancel === "string" && event.target.nodeName ? $(event.target).closest(this.options.cancel).length : false);
			if (!btnIsLeft || elIsCancel || !this._mouseCapture(event)) {
				return true;
			}

			this.mouseDelayMet = !this.options.delay;
			if (!this.mouseDelayMet) {
				this._mouseDelayTimer = setTimeout(function() {
					that.mouseDelayMet = true;
				}, this.options.delay);
			}

			if (this._mouseDistanceMet(event) && this._mouseDelayMet(event)) {
				this._mouseStarted = (this._mouseStart(event) !== false);
				if (!this._mouseStarted) {
					event.preventDefault();
					return true;
				}
			}

			// Click event may never have fired (Gecko & Opera)
			if (true === $.data(event.target, this.widgetName + ".preventClickEvent")) {
				$.removeData(event.target, this.widgetName + ".preventClickEvent");
			}

			// these delegates are required to keep context
			this._mouseMoveDelegate = function(event) {
				return that._mouseMove(event);
			};
			this._mouseUpDelegate = function(event) {
				return that._mouseUp(event);
			};
			$(document)
				.bind("mousemove."+this.widgetName, this._mouseMoveDelegate)
				.bind("mouseup."+this.widgetName, this._mouseUpDelegate);

			event.preventDefault();

			mouseHandled = true;
			return true;
		},

		_mouseMove: function(event) {
			// IE mouseup check - mouseup happened when mouse was out of window
			if ($.ui.ie && ( !document.documentMode || document.documentMode < 9 ) && !event.button) {
				return this._mouseUp(event);
			}

			if (this._mouseStarted) {
				this._mouseDrag(event);
				return event.preventDefault();
			}

			if (this._mouseDistanceMet(event) && this._mouseDelayMet(event)) {
				this._mouseStarted =
					(this._mouseStart(this._mouseDownEvent, event) !== false);
				(this._mouseStarted ? this._mouseDrag(event) : this._mouseUp(event));
			}

			return !this._mouseStarted;
		},

		_mouseUp: function(event) {
			$(document)
				.unbind("mousemove."+this.widgetName, this._mouseMoveDelegate)
				.unbind("mouseup."+this.widgetName, this._mouseUpDelegate);

			if (this._mouseStarted) {
				this._mouseStarted = false;

				if (event.target === this._mouseDownEvent.target) {
					$.data(event.target, this.widgetName + ".preventClickEvent", true);
				}

				this._mouseStop(event);
			}

			return false;
		},

		_mouseDistanceMet: function(event) {
			return (Math.max(
					Math.abs(this._mouseDownEvent.pageX - event.pageX),
					Math.abs(this._mouseDownEvent.pageY - event.pageY)
				) >= this.options.distance
			);
		},

		_mouseDelayMet: function(/* event */) {
			return this.mouseDelayMet;
		},

		// These are placeholder methods, to be overriden by extending plugin
		_mouseStart: function(/* event */) {},
		_mouseDrag: function(/* event */) {},
		_mouseStop: function(/* event */) {},
		_mouseCapture: function(/* event */) { return true; }
	});

	})(jQuery);
	;
	/*!
	 * jQuery UI Position 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/position/
	 */
	(function( $, undefined ) {

	$.ui = $.ui || {};

	var cachedScrollbarWidth,
		max = Math.max,
		abs = Math.abs,
		round = Math.round,
		rhorizontal = /left|center|right/,
		rvertical = /top|center|bottom/,
		roffset = /[\+\-]\d+(\.[\d]+)?%?/,
		rposition = /^\w+/,
		rpercent = /%$/,
		_position = $.fn.position;

	function getOffsets( offsets, width, height ) {
		return [
			parseFloat( offsets[ 0 ] ) * ( rpercent.test( offsets[ 0 ] ) ? width / 100 : 1 ),
			parseFloat( offsets[ 1 ] ) * ( rpercent.test( offsets[ 1 ] ) ? height / 100 : 1 )
		];
	}

	function parseCss( element, property ) {
		return parseInt( $.css( element, property ), 10 ) || 0;
	}

	function getDimensions( elem ) {
		var raw = elem[0];
		if ( raw.nodeType === 9 ) {
			return {
				width: elem.width(),
				height: elem.height(),
				offset: { top: 0, left: 0 }
			};
		}
		if ( $.isWindow( raw ) ) {
			return {
				width: elem.width(),
				height: elem.height(),
				offset: { top: elem.scrollTop(), left: elem.scrollLeft() }
			};
		}
		if ( raw.preventDefault ) {
			return {
				width: 0,
				height: 0,
				offset: { top: raw.pageY, left: raw.pageX }
			};
		}
		return {
			width: elem.outerWidth(),
			height: elem.outerHeight(),
			offset: elem.offset()
		};
	}

	$.position = {
		scrollbarWidth: function() {
			if ( cachedScrollbarWidth !== undefined ) {
				return cachedScrollbarWidth;
			}
			var w1, w2,
				div = $( "<div style='display:block;position:absolute;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>" ),
				innerDiv = div.children()[0];

			$( "body" ).append( div );
			w1 = innerDiv.offsetWidth;
			div.css( "overflow", "scroll" );

			w2 = innerDiv.offsetWidth;

			if ( w1 === w2 ) {
				w2 = div[0].clientWidth;
			}

			div.remove();

			return (cachedScrollbarWidth = w1 - w2);
		},
		getScrollInfo: function( within ) {
			var overflowX = within.isWindow || within.isDocument ? "" :
					within.element.css( "overflow-x" ),
				overflowY = within.isWindow || within.isDocument ? "" :
					within.element.css( "overflow-y" ),
				hasOverflowX = overflowX === "scroll" ||
					( overflowX === "auto" && within.width < within.element[0].scrollWidth ),
				hasOverflowY = overflowY === "scroll" ||
					( overflowY === "auto" && within.height < within.element[0].scrollHeight );
			return {
				width: hasOverflowY ? $.position.scrollbarWidth() : 0,
				height: hasOverflowX ? $.position.scrollbarWidth() : 0
			};
		},
		getWithinInfo: function( element ) {
			var withinElement = $( element || window ),
				isWindow = $.isWindow( withinElement[0] ),
				isDocument = !!withinElement[ 0 ] && withinElement[ 0 ].nodeType === 9;
			return {
				element: withinElement,
				isWindow: isWindow,
				isDocument: isDocument,
				offset: withinElement.offset() || { left: 0, top: 0 },
				scrollLeft: withinElement.scrollLeft(),
				scrollTop: withinElement.scrollTop(),
				width: isWindow ? withinElement.width() : withinElement.outerWidth(),
				height: isWindow ? withinElement.height() : withinElement.outerHeight()
			};
		}
	};

	$.fn.position = function( options ) {
		if ( !options || !options.of ) {
			return _position.apply( this, arguments );
		}

		// make a copy, we don't want to modify arguments
		options = $.extend( {}, options );

		var atOffset, targetWidth, targetHeight, targetOffset, basePosition, dimensions,
			target = $( options.of ),
			within = $.position.getWithinInfo( options.within ),
			scrollInfo = $.position.getScrollInfo( within ),
			collision = ( options.collision || "flip" ).split( " " ),
			offsets = {};

		dimensions = getDimensions( target );
		if ( target[0].preventDefault ) {
			// force left top to allow flipping
			options.at = "left top";
		}
		targetWidth = dimensions.width;
		targetHeight = dimensions.height;
		targetOffset = dimensions.offset;
		// clone to reuse original targetOffset later
		basePosition = $.extend( {}, targetOffset );

		// force my and at to have valid horizontal and vertical positions
		// if a value is missing or invalid, it will be converted to center
		$.each( [ "my", "at" ], function() {
			var pos = ( options[ this ] || "" ).split( " " ),
				horizontalOffset,
				verticalOffset;

			if ( pos.length === 1) {
				pos = rhorizontal.test( pos[ 0 ] ) ?
					pos.concat( [ "center" ] ) :
					rvertical.test( pos[ 0 ] ) ?
						[ "center" ].concat( pos ) :
						[ "center", "center" ];
			}
			pos[ 0 ] = rhorizontal.test( pos[ 0 ] ) ? pos[ 0 ] : "center";
			pos[ 1 ] = rvertical.test( pos[ 1 ] ) ? pos[ 1 ] : "center";

			// calculate offsets
			horizontalOffset = roffset.exec( pos[ 0 ] );
			verticalOffset = roffset.exec( pos[ 1 ] );
			offsets[ this ] = [
				horizontalOffset ? horizontalOffset[ 0 ] : 0,
				verticalOffset ? verticalOffset[ 0 ] : 0
			];

			// reduce to just the positions without the offsets
			options[ this ] = [
				rposition.exec( pos[ 0 ] )[ 0 ],
				rposition.exec( pos[ 1 ] )[ 0 ]
			];
		});

		// normalize collision option
		if ( collision.length === 1 ) {
			collision[ 1 ] = collision[ 0 ];
		}

		if ( options.at[ 0 ] === "right" ) {
			basePosition.left += targetWidth;
		} else if ( options.at[ 0 ] === "center" ) {
			basePosition.left += targetWidth / 2;
		}

		if ( options.at[ 1 ] === "bottom" ) {
			basePosition.top += targetHeight;
		} else if ( options.at[ 1 ] === "center" ) {
			basePosition.top += targetHeight / 2;
		}

		atOffset = getOffsets( offsets.at, targetWidth, targetHeight );
		basePosition.left += atOffset[ 0 ];
		basePosition.top += atOffset[ 1 ];

		return this.each(function() {
			var collisionPosition, using,
				elem = $( this ),
				elemWidth = elem.outerWidth(),
				elemHeight = elem.outerHeight(),
				marginLeft = parseCss( this, "marginLeft" ),
				marginTop = parseCss( this, "marginTop" ),
				collisionWidth = elemWidth + marginLeft + parseCss( this, "marginRight" ) + scrollInfo.width,
				collisionHeight = elemHeight + marginTop + parseCss( this, "marginBottom" ) + scrollInfo.height,
				position = $.extend( {}, basePosition ),
				myOffset = getOffsets( offsets.my, elem.outerWidth(), elem.outerHeight() );

			if ( options.my[ 0 ] === "right" ) {
				position.left -= elemWidth;
			} else if ( options.my[ 0 ] === "center" ) {
				position.left -= elemWidth / 2;
			}

			if ( options.my[ 1 ] === "bottom" ) {
				position.top -= elemHeight;
			} else if ( options.my[ 1 ] === "center" ) {
				position.top -= elemHeight / 2;
			}

			position.left += myOffset[ 0 ];
			position.top += myOffset[ 1 ];

			// if the browser doesn't support fractions, then round for consistent results
			if ( !$.support.offsetFractions ) {
				position.left = round( position.left );
				position.top = round( position.top );
			}

			collisionPosition = {
				marginLeft: marginLeft,
				marginTop: marginTop
			};

			$.each( [ "left", "top" ], function( i, dir ) {
				if ( $.ui.position[ collision[ i ] ] ) {
					$.ui.position[ collision[ i ] ][ dir ]( position, {
						targetWidth: targetWidth,
						targetHeight: targetHeight,
						elemWidth: elemWidth,
						elemHeight: elemHeight,
						collisionPosition: collisionPosition,
						collisionWidth: collisionWidth,
						collisionHeight: collisionHeight,
						offset: [ atOffset[ 0 ] + myOffset[ 0 ], atOffset [ 1 ] + myOffset[ 1 ] ],
						my: options.my,
						at: options.at,
						within: within,
						elem : elem
					});
				}
			});

			if ( options.using ) {
				// adds feedback as second argument to using callback, if present
				using = function( props ) {
					var left = targetOffset.left - position.left,
						right = left + targetWidth - elemWidth,
						top = targetOffset.top - position.top,
						bottom = top + targetHeight - elemHeight,
						feedback = {
							target: {
								element: target,
								left: targetOffset.left,
								top: targetOffset.top,
								width: targetWidth,
								height: targetHeight
							},
							element: {
								element: elem,
								left: position.left,
								top: position.top,
								width: elemWidth,
								height: elemHeight
							},
							horizontal: right < 0 ? "left" : left > 0 ? "right" : "center",
							vertical: bottom < 0 ? "top" : top > 0 ? "bottom" : "middle"
						};
					if ( targetWidth < elemWidth && abs( left + right ) < targetWidth ) {
						feedback.horizontal = "center";
					}
					if ( targetHeight < elemHeight && abs( top + bottom ) < targetHeight ) {
						feedback.vertical = "middle";
					}
					if ( max( abs( left ), abs( right ) ) > max( abs( top ), abs( bottom ) ) ) {
						feedback.important = "horizontal";
					} else {
						feedback.important = "vertical";
					}
					options.using.call( this, props, feedback );
				};
			}

			elem.offset( $.extend( position, { using: using } ) );
		});
	};

	$.ui.position = {
		fit: {
			left: function( position, data ) {
				var within = data.within,
					withinOffset = within.isWindow ? within.scrollLeft : within.offset.left,
					outerWidth = within.width,
					collisionPosLeft = position.left - data.collisionPosition.marginLeft,
					overLeft = withinOffset - collisionPosLeft,
					overRight = collisionPosLeft + data.collisionWidth - outerWidth - withinOffset,
					newOverRight;

				// element is wider than within
				if ( data.collisionWidth > outerWidth ) {
					// element is initially over the left side of within
					if ( overLeft > 0 && overRight <= 0 ) {
						newOverRight = position.left + overLeft + data.collisionWidth - outerWidth - withinOffset;
						position.left += overLeft - newOverRight;
					// element is initially over right side of within
					} else if ( overRight > 0 && overLeft <= 0 ) {
						position.left = withinOffset;
					// element is initially over both left and right sides of within
					} else {
						if ( overLeft > overRight ) {
							position.left = withinOffset + outerWidth - data.collisionWidth;
						} else {
							position.left = withinOffset;
						}
					}
				// too far left -> align with left edge
				} else if ( overLeft > 0 ) {
					position.left += overLeft;
				// too far right -> align with right edge
				} else if ( overRight > 0 ) {
					position.left -= overRight;
				// adjust based on position and margin
				} else {
					position.left = max( position.left - collisionPosLeft, position.left );
				}
			},
			top: function( position, data ) {
				var within = data.within,
					withinOffset = within.isWindow ? within.scrollTop : within.offset.top,
					outerHeight = data.within.height,
					collisionPosTop = position.top - data.collisionPosition.marginTop,
					overTop = withinOffset - collisionPosTop,
					overBottom = collisionPosTop + data.collisionHeight - outerHeight - withinOffset,
					newOverBottom;

				// element is taller than within
				if ( data.collisionHeight > outerHeight ) {
					// element is initially over the top of within
					if ( overTop > 0 && overBottom <= 0 ) {
						newOverBottom = position.top + overTop + data.collisionHeight - outerHeight - withinOffset;
						position.top += overTop - newOverBottom;
					// element is initially over bottom of within
					} else if ( overBottom > 0 && overTop <= 0 ) {
						position.top = withinOffset;
					// element is initially over both top and bottom of within
					} else {
						if ( overTop > overBottom ) {
							position.top = withinOffset + outerHeight - data.collisionHeight;
						} else {
							position.top = withinOffset;
						}
					}
				// too far up -> align with top
				} else if ( overTop > 0 ) {
					position.top += overTop;
				// too far down -> align with bottom edge
				} else if ( overBottom > 0 ) {
					position.top -= overBottom;
				// adjust based on position and margin
				} else {
					position.top = max( position.top - collisionPosTop, position.top );
				}
			}
		},
		flip: {
			left: function( position, data ) {
				var within = data.within,
					withinOffset = within.offset.left + within.scrollLeft,
					outerWidth = within.width,
					offsetLeft = within.isWindow ? within.scrollLeft : within.offset.left,
					collisionPosLeft = position.left - data.collisionPosition.marginLeft,
					overLeft = collisionPosLeft - offsetLeft,
					overRight = collisionPosLeft + data.collisionWidth - outerWidth - offsetLeft,
					myOffset = data.my[ 0 ] === "left" ?
						-data.elemWidth :
						data.my[ 0 ] === "right" ?
							data.elemWidth :
							0,
					atOffset = data.at[ 0 ] === "left" ?
						data.targetWidth :
						data.at[ 0 ] === "right" ?
							-data.targetWidth :
							0,
					offset = -2 * data.offset[ 0 ],
					newOverRight,
					newOverLeft;

				if ( overLeft < 0 ) {
					newOverRight = position.left + myOffset + atOffset + offset + data.collisionWidth - outerWidth - withinOffset;
					if ( newOverRight < 0 || newOverRight < abs( overLeft ) ) {
						position.left += myOffset + atOffset + offset;
					}
				}
				else if ( overRight > 0 ) {
					newOverLeft = position.left - data.collisionPosition.marginLeft + myOffset + atOffset + offset - offsetLeft;
					if ( newOverLeft > 0 || abs( newOverLeft ) < overRight ) {
						position.left += myOffset + atOffset + offset;
					}
				}
			},
			top: function( position, data ) {
				var within = data.within,
					withinOffset = within.offset.top + within.scrollTop,
					outerHeight = within.height,
					offsetTop = within.isWindow ? within.scrollTop : within.offset.top,
					collisionPosTop = position.top - data.collisionPosition.marginTop,
					overTop = collisionPosTop - offsetTop,
					overBottom = collisionPosTop + data.collisionHeight - outerHeight - offsetTop,
					top = data.my[ 1 ] === "top",
					myOffset = top ?
						-data.elemHeight :
						data.my[ 1 ] === "bottom" ?
							data.elemHeight :
							0,
					atOffset = data.at[ 1 ] === "top" ?
						data.targetHeight :
						data.at[ 1 ] === "bottom" ?
							-data.targetHeight :
							0,
					offset = -2 * data.offset[ 1 ],
					newOverTop,
					newOverBottom;
				if ( overTop < 0 ) {
					newOverBottom = position.top + myOffset + atOffset + offset + data.collisionHeight - outerHeight - withinOffset;
					if ( ( position.top + myOffset + atOffset + offset) > overTop && ( newOverBottom < 0 || newOverBottom < abs( overTop ) ) ) {
						position.top += myOffset + atOffset + offset;
					}
				}
				else if ( overBottom > 0 ) {
					newOverTop = position.top - data.collisionPosition.marginTop + myOffset + atOffset + offset - offsetTop;
					if ( ( position.top + myOffset + atOffset + offset) > overBottom && ( newOverTop > 0 || abs( newOverTop ) < overBottom ) ) {
						position.top += myOffset + atOffset + offset;
					}
				}
			}
		},
		flipfit: {
			left: function() {
				$.ui.position.flip.left.apply( this, arguments );
				$.ui.position.fit.left.apply( this, arguments );
			},
			top: function() {
				$.ui.position.flip.top.apply( this, arguments );
				$.ui.position.fit.top.apply( this, arguments );
			}
		}
	};

	// fraction support test
	(function () {
		var testElement, testElementParent, testElementStyle, offsetLeft, i,
			body = document.getElementsByTagName( "body" )[ 0 ],
			div = document.createElement( "div" );

		//Create a "fake body" for testing based on method used in jQuery.support
		testElement = document.createElement( body ? "div" : "body" );
		testElementStyle = {
			visibility: "hidden",
			width: 0,
			height: 0,
			border: 0,
			margin: 0,
			background: "none"
		};
		if ( body ) {
			$.extend( testElementStyle, {
				position: "absolute",
				left: "-1000px",
				top: "-1000px"
			});
		}
		for ( i in testElementStyle ) {
			testElement.style[ i ] = testElementStyle[ i ];
		}
		testElement.appendChild( div );
		testElementParent = body || document.documentElement;
		testElementParent.insertBefore( testElement, testElementParent.firstChild );

		div.style.cssText = "position: absolute; left: 10.7432222px;";

		offsetLeft = $( div ).offset().left;
		$.support.offsetFractions = offsetLeft > 10 && offsetLeft < 11;

		testElement.innerHTML = "";
		testElementParent.removeChild( testElement );
	})();

	}( jQuery ) );
	;
	/*!
	 * jQuery UI Sortable 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/sortable/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.mouse.js
	 *	jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	function isOverAxis( x, reference, size ) {
		return ( x > reference ) && ( x < ( reference + size ) );
	}

	function isFloating(item) {
		return (/left|right/).test(item.css("float")) || (/inline|table-cell/).test(item.css("display"));
	}

	$.widget("ui.sortable", $.ui.mouse, {
		version: "1.10.4",
		widgetEventPrefix: "sort",
		ready: false,
		options: {
			appendTo: "parent",
			axis: false,
			connectWith: false,
			containment: false,
			cursor: "auto",
			cursorAt: false,
			dropOnEmpty: true,
			forcePlaceholderSize: false,
			forceHelperSize: false,
			grid: false,
			handle: false,
			helper: "original",
			items: "> *",
			opacity: false,
			placeholder: false,
			revert: false,
			scroll: true,
			scrollSensitivity: 20,
			scrollSpeed: 20,
			scope: "default",
			tolerance: "intersect",
			zIndex: 1000,

			// callbacks
			activate: null,
			beforeStop: null,
			change: null,
			deactivate: null,
			out: null,
			over: null,
			receive: null,
			remove: null,
			sort: null,
			start: null,
			stop: null,
			update: null
		},
		_create: function() {

			var o = this.options;
			this.containerCache = {};
			this.element.addClass("ui-sortable");

			//Get the items
			this.refresh();

			//Let's determine if the items are being displayed horizontally
			this.floating = this.items.length ? o.axis === "x" || isFloating(this.items[0].item) : false;

			//Let's determine the parent's offset
			this.offset = this.element.offset();

			//Initialize mouse events for interaction
			this._mouseInit();

			//We're ready to go
			this.ready = true;

		},

		_destroy: function() {
			this.element
				.removeClass("ui-sortable ui-sortable-disabled");
			this._mouseDestroy();

			for ( var i = this.items.length - 1; i >= 0; i-- ) {
				this.items[i].item.removeData(this.widgetName + "-item");
			}

			return this;
		},

		_setOption: function(key, value){
			if ( key === "disabled" ) {
				this.options[ key ] = value;

				this.widget().toggleClass( "ui-sortable-disabled", !!value );
			} else {
				// Don't call widget base _setOption for disable as it adds ui-state-disabled class
				$.Widget.prototype._setOption.apply(this, arguments);
			}
		},

		_mouseCapture: function(event, overrideHandle) {
			var currentItem = null,
				validHandle = false,
				that = this;

			if (this.reverting) {
				return false;
			}

			if(this.options.disabled || this.options.type === "static") {
				return false;
			}

			//We have to refresh the items data once first
			this._refreshItems(event);

			//Find out if the clicked node (or one of its parents) is a actual item in this.items
			$(event.target).parents().each(function() {
				if($.data(this, that.widgetName + "-item") === that) {
					currentItem = $(this);
					return false;
				}
			});
			if($.data(event.target, that.widgetName + "-item") === that) {
				currentItem = $(event.target);
			}

			if(!currentItem) {
				return false;
			}
			if(this.options.handle && !overrideHandle) {
				$(this.options.handle, currentItem).find("*").addBack().each(function() {
					if(this === event.target) {
						validHandle = true;
					}
				});
				if(!validHandle) {
					return false;
				}
			}

			this.currentItem = currentItem;
			this._removeCurrentsFromItems();
			return true;

		},

		_mouseStart: function(event, overrideHandle, noActivation) {

			var i, body,
				o = this.options;

			this.currentContainer = this;

			//We only need to call refreshPositions, because the refreshItems call has been moved to mouseCapture
			this.refreshPositions();

			//Create and append the visible helper
			this.helper = this._createHelper(event);

			//Cache the helper size
			this._cacheHelperProportions();

			/*
			 * - Position generation -
			 * This block generates everything position related - it's the core of draggables.
			 */

			//Cache the margins of the original element
			this._cacheMargins();

			//Get the next scrolling parent
			this.scrollParent = this.helper.scrollParent();

			//The element's absolute position on the page minus margins
			this.offset = this.currentItem.offset();
			this.offset = {
				top: this.offset.top - this.margins.top,
				left: this.offset.left - this.margins.left
			};

			$.extend(this.offset, {
				click: { //Where the click happened, relative to the element
					left: event.pageX - this.offset.left,
					top: event.pageY - this.offset.top
				},
				parent: this._getParentOffset(),
				relative: this._getRelativeOffset() //This is a relative to absolute position minus the actual position calculation - only used for relative positioned helper
			});

			// Only after we got the offset, we can change the helper's position to absolute
			// TODO: Still need to figure out a way to make relative sorting possible
			this.helper.css("position", "absolute");
			this.cssPosition = this.helper.css("position");

			//Generate the original position
			this.originalPosition = this._generatePosition(event);
			this.originalPageX = event.pageX;
			this.originalPageY = event.pageY;

			//Adjust the mouse offset relative to the helper if "cursorAt" is supplied
			(o.cursorAt && this._adjustOffsetFromHelper(o.cursorAt));

			//Cache the former DOM position
			this.domPosition = { prev: this.currentItem.prev()[0], parent: this.currentItem.parent()[0] };

			//If the helper is not the original, hide the original so it's not playing any role during the drag, won't cause anything bad this way
			if(this.helper[0] !== this.currentItem[0]) {
				this.currentItem.hide();
			}

			//Create the placeholder
			this._createPlaceholder();

			//Set a containment if given in the options
			if(o.containment) {
				this._setContainment();
			}

			if( o.cursor && o.cursor !== "auto" ) { // cursor option
				body = this.document.find( "body" );

				// support: IE
				this.storedCursor = body.css( "cursor" );
				body.css( "cursor", o.cursor );

				this.storedStylesheet = $( "<style>*{ cursor: "+o.cursor+" !important; }</style>" ).appendTo( body );
			}

			if(o.opacity) { // opacity option
				if (this.helper.css("opacity")) {
					this._storedOpacity = this.helper.css("opacity");
				}
				this.helper.css("opacity", o.opacity);
			}

			if(o.zIndex) { // zIndex option
				if (this.helper.css("zIndex")) {
					this._storedZIndex = this.helper.css("zIndex");
				}
				this.helper.css("zIndex", o.zIndex);
			}

			//Prepare scrolling
			if(this.scrollParent[0] !== document && this.scrollParent[0].tagName !== "HTML") {
				this.overflowOffset = this.scrollParent.offset();
			}

			//Call callbacks
			this._trigger("start", event, this._uiHash());

			//Recache the helper size
			if(!this._preserveHelperProportions) {
				this._cacheHelperProportions();
			}


			//Post "activate" events to possible containers
			if( !noActivation ) {
				for ( i = this.containers.length - 1; i >= 0; i-- ) {
					this.containers[ i ]._trigger( "activate", event, this._uiHash( this ) );
				}
			}

			//Prepare possible droppables
			if($.ui.ddmanager) {
				$.ui.ddmanager.current = this;
			}

			if ($.ui.ddmanager && !o.dropBehaviour) {
				$.ui.ddmanager.prepareOffsets(this, event);
			}

			this.dragging = true;

			this.helper.addClass("ui-sortable-helper");
			this._mouseDrag(event); //Execute the drag once - this causes the helper not to be visible before getting its correct position
			return true;

		},

		_mouseDrag: function(event) {
			var i, item, itemElement, intersection,
				o = this.options,
				scrolled = false;

			//Compute the helpers position
			this.position = this._generatePosition(event);
			this.positionAbs = this._convertPositionTo("absolute");

			if (!this.lastPositionAbs) {
				this.lastPositionAbs = this.positionAbs;
			}

			//Do scrolling
			if(this.options.scroll) {
				if(this.scrollParent[0] !== document && this.scrollParent[0].tagName !== "HTML") {

					if((this.overflowOffset.top + this.scrollParent[0].offsetHeight) - event.pageY < o.scrollSensitivity) {
						this.scrollParent[0].scrollTop = scrolled = this.scrollParent[0].scrollTop + o.scrollSpeed;
					} else if(event.pageY - this.overflowOffset.top < o.scrollSensitivity) {
						this.scrollParent[0].scrollTop = scrolled = this.scrollParent[0].scrollTop - o.scrollSpeed;
					}

					if((this.overflowOffset.left + this.scrollParent[0].offsetWidth) - event.pageX < o.scrollSensitivity) {
						this.scrollParent[0].scrollLeft = scrolled = this.scrollParent[0].scrollLeft + o.scrollSpeed;
					} else if(event.pageX - this.overflowOffset.left < o.scrollSensitivity) {
						this.scrollParent[0].scrollLeft = scrolled = this.scrollParent[0].scrollLeft - o.scrollSpeed;
					}

				} else {

					if(event.pageY - $(document).scrollTop() < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() - o.scrollSpeed);
					} else if($(window).height() - (event.pageY - $(document).scrollTop()) < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() + o.scrollSpeed);
					}

					if(event.pageX - $(document).scrollLeft() < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() - o.scrollSpeed);
					} else if($(window).width() - (event.pageX - $(document).scrollLeft()) < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() + o.scrollSpeed);
					}

				}

				if(scrolled !== false && $.ui.ddmanager && !o.dropBehaviour) {
					$.ui.ddmanager.prepareOffsets(this, event);
				}
			}

			//Regenerate the absolute position used for position checks
			this.positionAbs = this._convertPositionTo("absolute");

			//Set the helper position
			if(!this.options.axis || this.options.axis !== "y") {
				this.helper[0].style.left = this.position.left+"px";
			}
			if(!this.options.axis || this.options.axis !== "x") {
				this.helper[0].style.top = this.position.top+"px";
			}

			//Rearrange
			for (i = this.items.length - 1; i >= 0; i--) {

				//Cache variables and intersection, continue if no intersection
				item = this.items[i];
				itemElement = item.item[0];
				intersection = this._intersectsWithPointer(item);
				if (!intersection) {
					continue;
				}

				// Only put the placeholder inside the current Container, skip all
				// items from other containers. This works because when moving
				// an item from one container to another the
				// currentContainer is switched before the placeholder is moved.
				//
				// Without this, moving items in "sub-sortables" can cause
				// the placeholder to jitter beetween the outer and inner container.
				if (item.instance !== this.currentContainer) {
					continue;
				}

				// cannot intersect with itself
				// no useless actions that have been done before
				// no action if the item moved is the parent of the item checked
				if (itemElement !== this.currentItem[0] &&
					this.placeholder[intersection === 1 ? "next" : "prev"]()[0] !== itemElement &&
					!$.contains(this.placeholder[0], itemElement) &&
					(this.options.type === "semi-dynamic" ? !$.contains(this.element[0], itemElement) : true)
				) {

					this.direction = intersection === 1 ? "down" : "up";

					if (this.options.tolerance === "pointer" || this._intersectsWithSides(item)) {
						this._rearrange(event, item);
					} else {
						break;
					}

					this._trigger("change", event, this._uiHash());
					break;
				}
			}

			//Post events to containers
			this._contactContainers(event);

			//Interconnect with droppables
			if($.ui.ddmanager) {
				$.ui.ddmanager.drag(this, event);
			}

			//Call callbacks
			this._trigger("sort", event, this._uiHash());

			this.lastPositionAbs = this.positionAbs;
			return false;

		},

		_mouseStop: function(event, noPropagation) {

			if(!event) {
				return;
			}

			//If we are using droppables, inform the manager about the drop
			if ($.ui.ddmanager && !this.options.dropBehaviour) {
				$.ui.ddmanager.drop(this, event);
			}

			if(this.options.revert) {
				var that = this,
					cur = this.placeholder.offset(),
					axis = this.options.axis,
					animation = {};

				if ( !axis || axis === "x" ) {
					animation.left = cur.left - this.offset.parent.left - this.margins.left + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollLeft);
				}
				if ( !axis || axis === "y" ) {
					animation.top = cur.top - this.offset.parent.top - this.margins.top + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollTop);
				}
				this.reverting = true;
				$(this.helper).animate( animation, parseInt(this.options.revert, 10) || 500, function() {
					that._clear(event);
				});
			} else {
				this._clear(event, noPropagation);
			}

			return false;

		},

		cancel: function() {

			if(this.dragging) {

				this._mouseUp({ target: null });

				if(this.options.helper === "original") {
					this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper");
				} else {
					this.currentItem.show();
				}

				//Post deactivating events to containers
				for (var i = this.containers.length - 1; i >= 0; i--){
					this.containers[i]._trigger("deactivate", null, this._uiHash(this));
					if(this.containers[i].containerCache.over) {
						this.containers[i]._trigger("out", null, this._uiHash(this));
						this.containers[i].containerCache.over = 0;
					}
				}

			}

			if (this.placeholder) {
				//$(this.placeholder[0]).remove(); would have been the jQuery way - unfortunately, it unbinds ALL events from the original node!
				if(this.placeholder[0].parentNode) {
					this.placeholder[0].parentNode.removeChild(this.placeholder[0]);
				}
				if(this.options.helper !== "original" && this.helper && this.helper[0].parentNode) {
					this.helper.remove();
				}

				$.extend(this, {
					helper: null,
					dragging: false,
					reverting: false,
					_noFinalSort: null
				});

				if(this.domPosition.prev) {
					$(this.domPosition.prev).after(this.currentItem);
				} else {
					$(this.domPosition.parent).prepend(this.currentItem);
				}
			}

			return this;

		},

		serialize: function(o) {

			var items = this._getItemsAsjQuery(o && o.connected),
				str = [];
			o = o || {};

			$(items).each(function() {
				var res = ($(o.item || this).attr(o.attribute || "id") || "").match(o.expression || (/(.+)[\-=_](.+)/));
				if (res) {
					str.push((o.key || res[1]+"[]")+"="+(o.key && o.expression ? res[1] : res[2]));
				}
			});

			if(!str.length && o.key) {
				str.push(o.key + "=");
			}

			return str.join("&");

		},

		toArray: function(o) {

			var items = this._getItemsAsjQuery(o && o.connected),
				ret = [];

			o = o || {};

			items.each(function() { ret.push($(o.item || this).attr(o.attribute || "id") || ""); });
			return ret;

		},

		/* Be careful with the following core functions */
		_intersectsWith: function(item) {

			var x1 = this.positionAbs.left,
				x2 = x1 + this.helperProportions.width,
				y1 = this.positionAbs.top,
				y2 = y1 + this.helperProportions.height,
				l = item.left,
				r = l + item.width,
				t = item.top,
				b = t + item.height,
				dyClick = this.offset.click.top,
				dxClick = this.offset.click.left,
				isOverElementHeight = ( this.options.axis === "x" ) || ( ( y1 + dyClick ) > t && ( y1 + dyClick ) < b ),
				isOverElementWidth = ( this.options.axis === "y" ) || ( ( x1 + dxClick ) > l && ( x1 + dxClick ) < r ),
				isOverElement = isOverElementHeight && isOverElementWidth;

			if ( this.options.tolerance === "pointer" ||
				this.options.forcePointerForContainers ||
				(this.options.tolerance !== "pointer" && this.helperProportions[this.floating ? "width" : "height"] > item[this.floating ? "width" : "height"])
			) {
				return isOverElement;
			} else {

				return (l < x1 + (this.helperProportions.width / 2) && // Right Half
					x2 - (this.helperProportions.width / 2) < r && // Left Half
					t < y1 + (this.helperProportions.height / 2) && // Bottom Half
					y2 - (this.helperProportions.height / 2) < b ); // Top Half

			}
		},

		_intersectsWithPointer: function(item) {

			var isOverElementHeight = (this.options.axis === "x") || isOverAxis(this.positionAbs.top + this.offset.click.top, item.top, item.height),
				isOverElementWidth = (this.options.axis === "y") || isOverAxis(this.positionAbs.left + this.offset.click.left, item.left, item.width),
				isOverElement = isOverElementHeight && isOverElementWidth,
				verticalDirection = this._getDragVerticalDirection(),
				horizontalDirection = this._getDragHorizontalDirection();

			if (!isOverElement) {
				return false;
			}

			return this.floating ?
				( ((horizontalDirection && horizontalDirection === "right") || verticalDirection === "down") ? 2 : 1 )
				: ( verticalDirection && (verticalDirection === "down" ? 2 : 1) );

		},

		_intersectsWithSides: function(item) {

			var isOverBottomHalf = isOverAxis(this.positionAbs.top + this.offset.click.top, item.top + (item.height/2), item.height),
				isOverRightHalf = isOverAxis(this.positionAbs.left + this.offset.click.left, item.left + (item.width/2), item.width),
				verticalDirection = this._getDragVerticalDirection(),
				horizontalDirection = this._getDragHorizontalDirection();

			if (this.floating && horizontalDirection) {
				return ((horizontalDirection === "right" && isOverRightHalf) || (horizontalDirection === "left" && !isOverRightHalf));
			} else {
				return verticalDirection && ((verticalDirection === "down" && isOverBottomHalf) || (verticalDirection === "up" && !isOverBottomHalf));
			}

		},

		_getDragVerticalDirection: function() {
			var delta = this.positionAbs.top - this.lastPositionAbs.top;
			return delta !== 0 && (delta > 0 ? "down" : "up");
		},

		_getDragHorizontalDirection: function() {
			var delta = this.positionAbs.left - this.lastPositionAbs.left;
			return delta !== 0 && (delta > 0 ? "right" : "left");
		},

		refresh: function(event) {
			this._refreshItems(event);
			this.refreshPositions();
			return this;
		},

		_connectWith: function() {
			var options = this.options;
			return options.connectWith.constructor === String ? [options.connectWith] : options.connectWith;
		},

		_getItemsAsjQuery: function(connected) {

			var i, j, cur, inst,
				items = [],
				queries = [],
				connectWith = this._connectWith();

			if(connectWith && connected) {
				for (i = connectWith.length - 1; i >= 0; i--){
					cur = $(connectWith[i]);
					for ( j = cur.length - 1; j >= 0; j--){
						inst = $.data(cur[j], this.widgetFullName);
						if(inst && inst !== this && !inst.options.disabled) {
							queries.push([$.isFunction(inst.options.items) ? inst.options.items.call(inst.element) : $(inst.options.items, inst.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), inst]);
						}
					}
				}
			}

			queries.push([$.isFunction(this.options.items) ? this.options.items.call(this.element, null, { options: this.options, item: this.currentItem }) : $(this.options.items, this.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), this]);

			function addItems() {
				items.push( this );
			}
			for (i = queries.length - 1; i >= 0; i--){
				queries[i][0].each( addItems );
			}

			return $(items);

		},

		_removeCurrentsFromItems: function() {

			var list = this.currentItem.find(":data(" + this.widgetName + "-item)");

			this.items = $.grep(this.items, function (item) {
				for (var j=0; j < list.length; j++) {
					if(list[j] === item.item[0]) {
						return false;
					}
				}
				return true;
			});

		},

		_refreshItems: function(event) {

			this.items = [];
			this.containers = [this];

			var i, j, cur, inst, targetData, _queries, item, queriesLength,
				items = this.items,
				queries = [[$.isFunction(this.options.items) ? this.options.items.call(this.element[0], event, { item: this.currentItem }) : $(this.options.items, this.element), this]],
				connectWith = this._connectWith();

			if(connectWith && this.ready) { //Shouldn't be run the first time through due to massive slow-down
				for (i = connectWith.length - 1; i >= 0; i--){
					cur = $(connectWith[i]);
					for (j = cur.length - 1; j >= 0; j--){
						inst = $.data(cur[j], this.widgetFullName);
						if(inst && inst !== this && !inst.options.disabled) {
							queries.push([$.isFunction(inst.options.items) ? inst.options.items.call(inst.element[0], event, { item: this.currentItem }) : $(inst.options.items, inst.element), inst]);
							this.containers.push(inst);
						}
					}
				}
			}

			for (i = queries.length - 1; i >= 0; i--) {
				targetData = queries[i][1];
				_queries = queries[i][0];

				for (j=0, queriesLength = _queries.length; j < queriesLength; j++) {
					item = $(_queries[j]);

					item.data(this.widgetName + "-item", targetData); // Data for target checking (mouse manager)

					items.push({
						item: item,
						instance: targetData,
						width: 0, height: 0,
						left: 0, top: 0
					});
				}
			}

		},

		refreshPositions: function(fast) {

			//This has to be redone because due to the item being moved out/into the offsetParent, the offsetParent's position will change
			if(this.offsetParent && this.helper) {
				this.offset.parent = this._getParentOffset();
			}

			var i, item, t, p;

			for (i = this.items.length - 1; i >= 0; i--){
				item = this.items[i];

				//We ignore calculating positions of all connected containers when we're not over them
				if(item.instance !== this.currentContainer && this.currentContainer && item.item[0] !== this.currentItem[0]) {
					continue;
				}

				t = this.options.toleranceElement ? $(this.options.toleranceElement, item.item) : item.item;

				if (!fast) {
					item.width = t.outerWidth();
					item.height = t.outerHeight();
				}

				p = t.offset();
				item.left = p.left;
				item.top = p.top;
			}

			if(this.options.custom && this.options.custom.refreshContainers) {
				this.options.custom.refreshContainers.call(this);
			} else {
				for (i = this.containers.length - 1; i >= 0; i--){
					p = this.containers[i].element.offset();
					this.containers[i].containerCache.left = p.left;
					this.containers[i].containerCache.top = p.top;
					this.containers[i].containerCache.width	= this.containers[i].element.outerWidth();
					this.containers[i].containerCache.height = this.containers[i].element.outerHeight();
				}
			}

			return this;
		},

		_createPlaceholder: function(that) {
			that = that || this;
			var className,
				o = that.options;

			if(!o.placeholder || o.placeholder.constructor === String) {
				className = o.placeholder;
				o.placeholder = {
					element: function() {

						var nodeName = that.currentItem[0].nodeName.toLowerCase(),
							element = $( "<" + nodeName + ">", that.document[0] )
								.addClass(className || that.currentItem[0].className+" ui-sortable-placeholder")
								.removeClass("ui-sortable-helper");

						if ( nodeName === "tr" ) {
							that.currentItem.children().each(function() {
								$( "<td>&#160;</td>", that.document[0] )
									.attr( "colspan", $( this ).attr( "colspan" ) || 1 )
									.appendTo( element );
							});
						} else if ( nodeName === "img" ) {
							element.attr( "src", that.currentItem.attr( "src" ) );
						}

						if ( !className ) {
							element.css( "visibility", "hidden" );
						}

						return element;
					},
					update: function(container, p) {

						// 1. If a className is set as 'placeholder option, we don't force sizes - the class is responsible for that
						// 2. The option 'forcePlaceholderSize can be enabled to force it even if a class name is specified
						if(className && !o.forcePlaceholderSize) {
							return;
						}

						//If the element doesn't have a actual height by itself (without styles coming from a stylesheet), it receives the inline height from the dragged item
						if(!p.height()) { p.height(that.currentItem.innerHeight() - parseInt(that.currentItem.css("paddingTop")||0, 10) - parseInt(that.currentItem.css("paddingBottom")||0, 10)); }
						if(!p.width()) { p.width(that.currentItem.innerWidth() - parseInt(that.currentItem.css("paddingLeft")||0, 10) - parseInt(that.currentItem.css("paddingRight")||0, 10)); }
					}
				};
			}

			//Create the placeholder
			that.placeholder = $(o.placeholder.element.call(that.element, that.currentItem));

			//Append it after the actual current item
			that.currentItem.after(that.placeholder);

			//Update the size of the placeholder (TODO: Logic to fuzzy, see line 316/317)
			o.placeholder.update(that, that.placeholder);

		},

		_contactContainers: function(event) {
			var i, j, dist, itemWithLeastDistance, posProperty, sizeProperty, base, cur, nearBottom, floating,
				innermostContainer = null,
				innermostIndex = null;

			// get innermost container that intersects with item
			for (i = this.containers.length - 1; i >= 0; i--) {

				// never consider a container that's located within the item itself
				if($.contains(this.currentItem[0], this.containers[i].element[0])) {
					continue;
				}

				if(this._intersectsWith(this.containers[i].containerCache)) {

					// if we've already found a container and it's more "inner" than this, then continue
					if(innermostContainer && $.contains(this.containers[i].element[0], innermostContainer.element[0])) {
						continue;
					}

					innermostContainer = this.containers[i];
					innermostIndex = i;

				} else {
					// container doesn't intersect. trigger "out" event if necessary
					if(this.containers[i].containerCache.over) {
						this.containers[i]._trigger("out", event, this._uiHash(this));
						this.containers[i].containerCache.over = 0;
					}
				}

			}

			// if no intersecting containers found, return
			if(!innermostContainer) {
				return;
			}

			// move the item into the container if it's not there already
			if(this.containers.length === 1) {
				if (!this.containers[innermostIndex].containerCache.over) {
					this.containers[innermostIndex]._trigger("over", event, this._uiHash(this));
					this.containers[innermostIndex].containerCache.over = 1;
				}
			} else {

				//When entering a new container, we will find the item with the least distance and append our item near it
				dist = 10000;
				itemWithLeastDistance = null;
				floating = innermostContainer.floating || isFloating(this.currentItem);
				posProperty = floating ? "left" : "top";
				sizeProperty = floating ? "width" : "height";
				base = this.positionAbs[posProperty] + this.offset.click[posProperty];
				for (j = this.items.length - 1; j >= 0; j--) {
					if(!$.contains(this.containers[innermostIndex].element[0], this.items[j].item[0])) {
						continue;
					}
					if(this.items[j].item[0] === this.currentItem[0]) {
						continue;
					}
					if (floating && !isOverAxis(this.positionAbs.top + this.offset.click.top, this.items[j].top, this.items[j].height)) {
						continue;
					}
					cur = this.items[j].item.offset()[posProperty];
					nearBottom = false;
					if(Math.abs(cur - base) > Math.abs(cur + this.items[j][sizeProperty] - base)){
						nearBottom = true;
						cur += this.items[j][sizeProperty];
					}

					if(Math.abs(cur - base) < dist) {
						dist = Math.abs(cur - base); itemWithLeastDistance = this.items[j];
						this.direction = nearBottom ? "up": "down";
					}
				}

				//Check if dropOnEmpty is enabled
				if(!itemWithLeastDistance && !this.options.dropOnEmpty) {
					return;
				}

				if(this.currentContainer === this.containers[innermostIndex]) {
					return;
				}

				itemWithLeastDistance ? this._rearrange(event, itemWithLeastDistance, null, true) : this._rearrange(event, null, this.containers[innermostIndex].element, true);
				this._trigger("change", event, this._uiHash());
				this.containers[innermostIndex]._trigger("change", event, this._uiHash(this));
				this.currentContainer = this.containers[innermostIndex];

				//Update the placeholder
				this.options.placeholder.update(this.currentContainer, this.placeholder);

				this.containers[innermostIndex]._trigger("over", event, this._uiHash(this));
				this.containers[innermostIndex].containerCache.over = 1;
			}


		},

		_createHelper: function(event) {

			var o = this.options,
				helper = $.isFunction(o.helper) ? $(o.helper.apply(this.element[0], [event, this.currentItem])) : (o.helper === "clone" ? this.currentItem.clone() : this.currentItem);

			//Add the helper to the DOM if that didn't happen already
			if(!helper.parents("body").length) {
				$(o.appendTo !== "parent" ? o.appendTo : this.currentItem[0].parentNode)[0].appendChild(helper[0]);
			}

			if(helper[0] === this.currentItem[0]) {
				this._storedCSS = { width: this.currentItem[0].style.width, height: this.currentItem[0].style.height, position: this.currentItem.css("position"), top: this.currentItem.css("top"), left: this.currentItem.css("left") };
			}

			if(!helper[0].style.width || o.forceHelperSize) {
				helper.width(this.currentItem.width());
			}
			if(!helper[0].style.height || o.forceHelperSize) {
				helper.height(this.currentItem.height());
			}

			return helper;

		},

		_adjustOffsetFromHelper: function(obj) {
			if (typeof obj === "string") {
				obj = obj.split(" ");
			}
			if ($.isArray(obj)) {
				obj = {left: +obj[0], top: +obj[1] || 0};
			}
			if ("left" in obj) {
				this.offset.click.left = obj.left + this.margins.left;
			}
			if ("right" in obj) {
				this.offset.click.left = this.helperProportions.width - obj.right + this.margins.left;
			}
			if ("top" in obj) {
				this.offset.click.top = obj.top + this.margins.top;
			}
			if ("bottom" in obj) {
				this.offset.click.top = this.helperProportions.height - obj.bottom + this.margins.top;
			}
		},

		_getParentOffset: function() {


			//Get the offsetParent and cache its position
			this.offsetParent = this.helper.offsetParent();
			var po = this.offsetParent.offset();

			// This is a special case where we need to modify a offset calculated on start, since the following happened:
			// 1. The position of the helper is absolute, so it's position is calculated based on the next positioned parent
			// 2. The actual offset parent is a child of the scroll parent, and the scroll parent isn't the document, which means that
			//    the scroll is included in the initial calculation of the offset of the parent, and never recalculated upon drag
			if(this.cssPosition === "absolute" && this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) {
				po.left += this.scrollParent.scrollLeft();
				po.top += this.scrollParent.scrollTop();
			}

			// This needs to be actually done for all browsers, since pageX/pageY includes this information
			// with an ugly IE fix
			if( this.offsetParent[0] === document.body || (this.offsetParent[0].tagName && this.offsetParent[0].tagName.toLowerCase() === "html" && $.ui.ie)) {
				po = { top: 0, left: 0 };
			}

			return {
				top: po.top + (parseInt(this.offsetParent.css("borderTopWidth"),10) || 0),
				left: po.left + (parseInt(this.offsetParent.css("borderLeftWidth"),10) || 0)
			};

		},

		_getRelativeOffset: function() {

			if(this.cssPosition === "relative") {
				var p = this.currentItem.position();
				return {
					top: p.top - (parseInt(this.helper.css("top"),10) || 0) + this.scrollParent.scrollTop(),
					left: p.left - (parseInt(this.helper.css("left"),10) || 0) + this.scrollParent.scrollLeft()
				};
			} else {
				return { top: 0, left: 0 };
			}

		},

		_cacheMargins: function() {
			this.margins = {
				left: (parseInt(this.currentItem.css("marginLeft"),10) || 0),
				top: (parseInt(this.currentItem.css("marginTop"),10) || 0)
			};
		},

		_cacheHelperProportions: function() {
			this.helperProportions = {
				width: this.helper.outerWidth(),
				height: this.helper.outerHeight()
			};
		},

		_setContainment: function() {

			var ce, co, over,
				o = this.options;
			if(o.containment === "parent") {
				o.containment = this.helper[0].parentNode;
			}
			if(o.containment === "document" || o.containment === "window") {
				this.containment = [
					0 - this.offset.relative.left - this.offset.parent.left,
					0 - this.offset.relative.top - this.offset.parent.top,
					$(o.containment === "document" ? document : window).width() - this.helperProportions.width - this.margins.left,
					($(o.containment === "document" ? document : window).height() || document.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top
				];
			}

			if(!(/^(document|window|parent)$/).test(o.containment)) {
				ce = $(o.containment)[0];
				co = $(o.containment).offset();
				over = ($(ce).css("overflow") !== "hidden");

				this.containment = [
					co.left + (parseInt($(ce).css("borderLeftWidth"),10) || 0) + (parseInt($(ce).css("paddingLeft"),10) || 0) - this.margins.left,
					co.top + (parseInt($(ce).css("borderTopWidth"),10) || 0) + (parseInt($(ce).css("paddingTop"),10) || 0) - this.margins.top,
					co.left+(over ? Math.max(ce.scrollWidth,ce.offsetWidth) : ce.offsetWidth) - (parseInt($(ce).css("borderLeftWidth"),10) || 0) - (parseInt($(ce).css("paddingRight"),10) || 0) - this.helperProportions.width - this.margins.left,
					co.top+(over ? Math.max(ce.scrollHeight,ce.offsetHeight) : ce.offsetHeight) - (parseInt($(ce).css("borderTopWidth"),10) || 0) - (parseInt($(ce).css("paddingBottom"),10) || 0) - this.helperProportions.height - this.margins.top
				];
			}

		},

		_convertPositionTo: function(d, pos) {

			if(!pos) {
				pos = this.position;
			}
			var mod = d === "absolute" ? 1 : -1,
				scroll = this.cssPosition === "absolute" && !(this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) ? this.offsetParent : this.scrollParent,
				scrollIsRootNode = (/(html|body)/i).test(scroll[0].tagName);

			return {
				top: (
					pos.top	+																// The absolute mouse position
					this.offset.relative.top * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top * mod -											// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : ( scrollIsRootNode ? 0 : scroll.scrollTop() ) ) * mod)
				),
				left: (
					pos.left +																// The absolute mouse position
					this.offset.relative.left * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left * mod	-										// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : scrollIsRootNode ? 0 : scroll.scrollLeft() ) * mod)
				)
			};

		},

		_generatePosition: function(event) {

			var top, left,
				o = this.options,
				pageX = event.pageX,
				pageY = event.pageY,
				scroll = this.cssPosition === "absolute" && !(this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) ? this.offsetParent : this.scrollParent, scrollIsRootNode = (/(html|body)/i).test(scroll[0].tagName);

			// This is another very weird special case that only happens for relative elements:
			// 1. If the css position is relative
			// 2. and the scroll parent is the document or similar to the offset parent
			// we have to refresh the relative offset during the scroll so there are no jumps
			if(this.cssPosition === "relative" && !(this.scrollParent[0] !== document && this.scrollParent[0] !== this.offsetParent[0])) {
				this.offset.relative = this._getRelativeOffset();
			}

			/*
			 * - Position constraining -
			 * Constrain the position to a mix of grid, containment.
			 */

			if(this.originalPosition) { //If we are not dragging yet, we won't check for options

				if(this.containment) {
					if(event.pageX - this.offset.click.left < this.containment[0]) {
						pageX = this.containment[0] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top < this.containment[1]) {
						pageY = this.containment[1] + this.offset.click.top;
					}
					if(event.pageX - this.offset.click.left > this.containment[2]) {
						pageX = this.containment[2] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top > this.containment[3]) {
						pageY = this.containment[3] + this.offset.click.top;
					}
				}

				if(o.grid) {
					top = this.originalPageY + Math.round((pageY - this.originalPageY) / o.grid[1]) * o.grid[1];
					pageY = this.containment ? ( (top - this.offset.click.top >= this.containment[1] && top - this.offset.click.top <= this.containment[3]) ? top : ((top - this.offset.click.top >= this.containment[1]) ? top - o.grid[1] : top + o.grid[1])) : top;

					left = this.originalPageX + Math.round((pageX - this.originalPageX) / o.grid[0]) * o.grid[0];
					pageX = this.containment ? ( (left - this.offset.click.left >= this.containment[0] && left - this.offset.click.left <= this.containment[2]) ? left : ((left - this.offset.click.left >= this.containment[0]) ? left - o.grid[0] : left + o.grid[0])) : left;
				}

			}

			return {
				top: (
					pageY -																// The absolute mouse position
					this.offset.click.top -													// Click offset (relative to the element)
					this.offset.relative.top	-											// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top +												// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : ( scrollIsRootNode ? 0 : scroll.scrollTop() ) ))
				),
				left: (
					pageX -																// The absolute mouse position
					this.offset.click.left -												// Click offset (relative to the element)
					this.offset.relative.left	-											// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left +												// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : scrollIsRootNode ? 0 : scroll.scrollLeft() ))
				)
			};

		},

		_rearrange: function(event, i, a, hardRefresh) {

			a ? a[0].appendChild(this.placeholder[0]) : i.item[0].parentNode.insertBefore(this.placeholder[0], (this.direction === "down" ? i.item[0] : i.item[0].nextSibling));

			//Various things done here to improve the performance:
			// 1. we create a setTimeout, that calls refreshPositions
			// 2. on the instance, we have a counter variable, that get's higher after every append
			// 3. on the local scope, we copy the counter variable, and check in the timeout, if it's still the same
			// 4. this lets only the last addition to the timeout stack through
			this.counter = this.counter ? ++this.counter : 1;
			var counter = this.counter;

			this._delay(function() {
				if(counter === this.counter) {
					this.refreshPositions(!hardRefresh); //Precompute after each DOM insertion, NOT on mousemove
				}
			});

		},

		_clear: function(event, noPropagation) {

			this.reverting = false;
			// We delay all events that have to be triggered to after the point where the placeholder has been removed and
			// everything else normalized again
			var i,
				delayedTriggers = [];

			// We first have to update the dom position of the actual currentItem
			// Note: don't do it if the current item is already removed (by a user), or it gets reappended (see #4088)
			if(!this._noFinalSort && this.currentItem.parent().length) {
				this.placeholder.before(this.currentItem);
			}
			this._noFinalSort = null;

			if(this.helper[0] === this.currentItem[0]) {
				for(i in this._storedCSS) {
					if(this._storedCSS[i] === "auto" || this._storedCSS[i] === "static") {
						this._storedCSS[i] = "";
					}
				}
				this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper");
			} else {
				this.currentItem.show();
			}

			if(this.fromOutside && !noPropagation) {
				delayedTriggers.push(function(event) { this._trigger("receive", event, this._uiHash(this.fromOutside)); });
			}
			if((this.fromOutside || this.domPosition.prev !== this.currentItem.prev().not(".ui-sortable-helper")[0] || this.domPosition.parent !== this.currentItem.parent()[0]) && !noPropagation) {
				delayedTriggers.push(function(event) { this._trigger("update", event, this._uiHash()); }); //Trigger update callback if the DOM position has changed
			}

			// Check if the items Container has Changed and trigger appropriate
			// events.
			if (this !== this.currentContainer) {
				if(!noPropagation) {
					delayedTriggers.push(function(event) { this._trigger("remove", event, this._uiHash()); });
					delayedTriggers.push((function(c) { return function(event) { c._trigger("receive", event, this._uiHash(this)); };  }).call(this, this.currentContainer));
					delayedTriggers.push((function(c) { return function(event) { c._trigger("update", event, this._uiHash(this));  }; }).call(this, this.currentContainer));
				}
			}


			//Post events to containers
			function delayEvent( type, instance, container ) {
				return function( event ) {
					container._trigger( type, event, instance._uiHash( instance ) );
				};
			}
			for (i = this.containers.length - 1; i >= 0; i--){
				if (!noPropagation) {
					delayedTriggers.push( delayEvent( "deactivate", this, this.containers[ i ] ) );
				}
				if(this.containers[i].containerCache.over) {
					delayedTriggers.push( delayEvent( "out", this, this.containers[ i ] ) );
					this.containers[i].containerCache.over = 0;
				}
			}

			//Do what was originally in plugins
			if ( this.storedCursor ) {
				this.document.find( "body" ).css( "cursor", this.storedCursor );
				this.storedStylesheet.remove();
			}
			if(this._storedOpacity) {
				this.helper.css("opacity", this._storedOpacity);
			}
			if(this._storedZIndex) {
				this.helper.css("zIndex", this._storedZIndex === "auto" ? "" : this._storedZIndex);
			}

			this.dragging = false;
			if(this.cancelHelperRemoval) {
				if(!noPropagation) {
					this._trigger("beforeStop", event, this._uiHash());
					for (i=0; i < delayedTriggers.length; i++) {
						delayedTriggers[i].call(this, event);
					} //Trigger all delayed events
					this._trigger("stop", event, this._uiHash());
				}

				this.fromOutside = false;
				return false;
			}

			if(!noPropagation) {
				this._trigger("beforeStop", event, this._uiHash());
			}

			//$(this.placeholder[0]).remove(); would have been the jQuery way - unfortunately, it unbinds ALL events from the original node!
			this.placeholder[0].parentNode.removeChild(this.placeholder[0]);

			if(this.helper[0] !== this.currentItem[0]) {
				this.helper.remove();
			}
			this.helper = null;

			if(!noPropagation) {
				for (i=0; i < delayedTriggers.length; i++) {
					delayedTriggers[i].call(this, event);
				} //Trigger all delayed events
				this._trigger("stop", event, this._uiHash());
			}

			this.fromOutside = false;
			return true;

		},

		_trigger: function() {
			if ($.Widget.prototype._trigger.apply(this, arguments) === false) {
				this.cancel();
			}
		},

		_uiHash: function(_inst) {
			var inst = _inst || this;
			return {
				helper: inst.helper,
				placeholder: inst.placeholder || $([]),
				position: inst.position,
				originalPosition: inst.originalPosition,
				offset: inst.positionAbs,
				item: inst.currentItem,
				sender: _inst ? _inst.element : null
			};
		}

	});

	})(jQuery);
	;
	/*!
	 * jQuery UI Slider 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/slider/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.mouse.js
	 *	jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	// number of pages in a slider
	// (how many times can you page up/down to go through the whole range)
	var numPages = 5;

	$.widget( "ui.slider", $.ui.mouse, {
		version: "1.10.4",
		widgetEventPrefix: "slide",

		options: {
			animate: false,
			distance: 0,
			max: 100,
			min: 0,
			orientation: "horizontal",
			range: false,
			step: 1,
			value: 0,
			values: null,

			// callbacks
			change: null,
			slide: null,
			start: null,
			stop: null
		},

		_create: function() {
			this._keySliding = false;
			this._mouseSliding = false;
			this._animateOff = true;
			this._handleIndex = null;
			this._detectOrientation();
			this._mouseInit();

			this.element
				.addClass( "ui-slider" +
					" ui-slider-" + this.orientation +
					" ui-widget" +
					" ui-widget-content" +
					" ui-corner-all");

			this._refresh();
			this._setOption( "disabled", this.options.disabled );

			this._animateOff = false;
		},

		_refresh: function() {
			this._createRange();
			this._createHandles();
			this._setupEvents();
			this._refreshValue();
		},

		_createHandles: function() {
			var i, handleCount,
				options = this.options,
				existingHandles = this.element.find( ".ui-slider-handle" ).addClass( "ui-state-default ui-corner-all" ),
				handle = "<a class='ui-slider-handle ui-state-default ui-corner-all' href='#'></a>",
				handles = [];

			handleCount = ( options.values && options.values.length ) || 1;

			if ( existingHandles.length > handleCount ) {
				existingHandles.slice( handleCount ).remove();
				existingHandles = existingHandles.slice( 0, handleCount );
			}

			for ( i = existingHandles.length; i < handleCount; i++ ) {
				handles.push( handle );
			}

			this.handles = existingHandles.add( $( handles.join( "" ) ).appendTo( this.element ) );

			this.handle = this.handles.eq( 0 );

			this.handles.each(function( i ) {
				$( this ).data( "ui-slider-handle-index", i );
			});
		},

		_createRange: function() {
			var options = this.options,
				classes = "";

			if ( options.range ) {
				if ( options.range === true ) {
					if ( !options.values ) {
						options.values = [ this._valueMin(), this._valueMin() ];
					} else if ( options.values.length && options.values.length !== 2 ) {
						options.values = [ options.values[0], options.values[0] ];
					} else if ( $.isArray( options.values ) ) {
						options.values = options.values.slice(0);
					}
				}

				if ( !this.range || !this.range.length ) {
					this.range = $( "<div></div>" )
						.appendTo( this.element );

					classes = "ui-slider-range" +
					// note: this isn't the most fittingly semantic framework class for this element,
					// but worked best visually with a variety of themes
					" ui-widget-header ui-corner-all";
				} else {
					this.range.removeClass( "ui-slider-range-min ui-slider-range-max" )
						// Handle range switching from true to min/max
						.css({
							"left": "",
							"bottom": ""
						});
				}

				this.range.addClass( classes +
					( ( options.range === "min" || options.range === "max" ) ? " ui-slider-range-" + options.range : "" ) );
			} else {
				if ( this.range ) {
					this.range.remove();
				}
				this.range = null;
			}
		},

		_setupEvents: function() {
			var elements = this.handles.add( this.range ).filter( "a" );
			this._off( elements );
			this._on( elements, this._handleEvents );
			this._hoverable( elements );
			this._focusable( elements );
		},

		_destroy: function() {
			this.handles.remove();
			if ( this.range ) {
				this.range.remove();
			}

			this.element
				.removeClass( "ui-slider" +
					" ui-slider-horizontal" +
					" ui-slider-vertical" +
					" ui-widget" +
					" ui-widget-content" +
					" ui-corner-all" );

			this._mouseDestroy();
		},

		_mouseCapture: function( event ) {
			var position, normValue, distance, closestHandle, index, allowed, offset, mouseOverHandle,
				that = this,
				o = this.options;

			if ( o.disabled ) {
				return false;
			}

			this.elementSize = {
				width: this.element.outerWidth(),
				height: this.element.outerHeight()
			};
			this.elementOffset = this.element.offset();

			position = { x: event.pageX, y: event.pageY };
			normValue = this._normValueFromMouse( position );
			distance = this._valueMax() - this._valueMin() + 1;
			this.handles.each(function( i ) {
				var thisDistance = Math.abs( normValue - that.values(i) );
				if (( distance > thisDistance ) ||
					( distance === thisDistance &&
						(i === that._lastChangedValue || that.values(i) === o.min ))) {
					distance = thisDistance;
					closestHandle = $( this );
					index = i;
				}
			});

			allowed = this._start( event, index );
			if ( allowed === false ) {
				return false;
			}
			this._mouseSliding = true;

			this._handleIndex = index;

			closestHandle
				.addClass( "ui-state-active" )
				.focus();

			offset = closestHandle.offset();
			mouseOverHandle = !$( event.target ).parents().addBack().is( ".ui-slider-handle" );
			this._clickOffset = mouseOverHandle ? { left: 0, top: 0 } : {
				left: event.pageX - offset.left - ( closestHandle.width() / 2 ),
				top: event.pageY - offset.top -
					( closestHandle.height() / 2 ) -
					( parseInt( closestHandle.css("borderTopWidth"), 10 ) || 0 ) -
					( parseInt( closestHandle.css("borderBottomWidth"), 10 ) || 0) +
					( parseInt( closestHandle.css("marginTop"), 10 ) || 0)
			};

			if ( !this.handles.hasClass( "ui-state-hover" ) ) {
				this._slide( event, index, normValue );
			}
			this._animateOff = true;
			return true;
		},

		_mouseStart: function() {
			return true;
		},

		_mouseDrag: function( event ) {
			var position = { x: event.pageX, y: event.pageY },
				normValue = this._normValueFromMouse( position );

			this._slide( event, this._handleIndex, normValue );

			return false;
		},

		_mouseStop: function( event ) {
			this.handles.removeClass( "ui-state-active" );
			this._mouseSliding = false;

			this._stop( event, this._handleIndex );
			this._change( event, this._handleIndex );

			this._handleIndex = null;
			this._clickOffset = null;
			this._animateOff = false;

			return false;
		},

		_detectOrientation: function() {
			this.orientation = ( this.options.orientation === "vertical" ) ? "vertical" : "horizontal";
		},

		_normValueFromMouse: function( position ) {
			var pixelTotal,
				pixelMouse,
				percentMouse,
				valueTotal,
				valueMouse;

			if ( this.orientation === "horizontal" ) {
				pixelTotal = this.elementSize.width;
				pixelMouse = position.x - this.elementOffset.left - ( this._clickOffset ? this._clickOffset.left : 0 );
			} else {
				pixelTotal = this.elementSize.height;
				pixelMouse = position.y - this.elementOffset.top - ( this._clickOffset ? this._clickOffset.top : 0 );
			}

			percentMouse = ( pixelMouse / pixelTotal );
			if ( percentMouse > 1 ) {
				percentMouse = 1;
			}
			if ( percentMouse < 0 ) {
				percentMouse = 0;
			}
			if ( this.orientation === "vertical" ) {
				percentMouse = 1 - percentMouse;
			}

			valueTotal = this._valueMax() - this._valueMin();
			valueMouse = this._valueMin() + percentMouse * valueTotal;

			return this._trimAlignValue( valueMouse );
		},

		_start: function( event, index ) {
			var uiHash = {
				handle: this.handles[ index ],
				value: this.value()
			};
			if ( this.options.values && this.options.values.length ) {
				uiHash.value = this.values( index );
				uiHash.values = this.values();
			}
			return this._trigger( "start", event, uiHash );
		},

		_slide: function( event, index, newVal ) {
			var otherVal,
				newValues,
				allowed;

			if ( this.options.values && this.options.values.length ) {
				otherVal = this.values( index ? 0 : 1 );

				if ( ( this.options.values.length === 2 && this.options.range === true ) &&
						( ( index === 0 && newVal > otherVal) || ( index === 1 && newVal < otherVal ) )
					) {
					newVal = otherVal;
				}

				if ( newVal !== this.values( index ) ) {
					newValues = this.values();
					newValues[ index ] = newVal;
					// A slide can be canceled by returning false from the slide callback
					allowed = this._trigger( "slide", event, {
						handle: this.handles[ index ],
						value: newVal,
						values: newValues
					} );
					otherVal = this.values( index ? 0 : 1 );
					if ( allowed !== false ) {
						this.values( index, newVal );
					}
				}
			} else {
				if ( newVal !== this.value() ) {
					// A slide can be canceled by returning false from the slide callback
					allowed = this._trigger( "slide", event, {
						handle: this.handles[ index ],
						value: newVal
					} );
					if ( allowed !== false ) {
						this.value( newVal );
					}
				}
			}
		},

		_stop: function( event, index ) {
			var uiHash = {
				handle: this.handles[ index ],
				value: this.value()
			};
			if ( this.options.values && this.options.values.length ) {
				uiHash.value = this.values( index );
				uiHash.values = this.values();
			}

			this._trigger( "stop", event, uiHash );
		},

		_change: function( event, index ) {
			if ( !this._keySliding && !this._mouseSliding ) {
				var uiHash = {
					handle: this.handles[ index ],
					value: this.value()
				};
				if ( this.options.values && this.options.values.length ) {
					uiHash.value = this.values( index );
					uiHash.values = this.values();
				}

				//store the last changed value index for reference when handles overlap
				this._lastChangedValue = index;

				this._trigger( "change", event, uiHash );
			}
		},

		value: function( newValue ) {
			if ( arguments.length ) {
				this.options.value = this._trimAlignValue( newValue );
				this._refreshValue();
				this._change( null, 0 );
				return;
			}

			return this._value();
		},

		values: function( index, newValue ) {
			var vals,
				newValues,
				i;

			if ( arguments.length > 1 ) {
				this.options.values[ index ] = this._trimAlignValue( newValue );
				this._refreshValue();
				this._change( null, index );
				return;
			}

			if ( arguments.length ) {
				if ( $.isArray( arguments[ 0 ] ) ) {
					vals = this.options.values;
					newValues = arguments[ 0 ];
					for ( i = 0; i < vals.length; i += 1 ) {
						vals[ i ] = this._trimAlignValue( newValues[ i ] );
						this._change( null, i );
					}
					this._refreshValue();
				} else {
					if ( this.options.values && this.options.values.length ) {
						return this._values( index );
					} else {
						return this.value();
					}
				}
			} else {
				return this._values();
			}
		},

		_setOption: function( key, value ) {
			var i,
				valsLength = 0;

			if ( key === "range" && this.options.range === true ) {
				if ( value === "min" ) {
					this.options.value = this._values( 0 );
					this.options.values = null;
				} else if ( value === "max" ) {
					this.options.value = this._values( this.options.values.length-1 );
					this.options.values = null;
				}
			}

			if ( $.isArray( this.options.values ) ) {
				valsLength = this.options.values.length;
			}

			$.Widget.prototype._setOption.apply( this, arguments );

			switch ( key ) {
				case "orientation":
					this._detectOrientation();
					this.element
						.removeClass( "ui-slider-horizontal ui-slider-vertical" )
						.addClass( "ui-slider-" + this.orientation );
					this._refreshValue();
					break;
				case "value":
					this._animateOff = true;
					this._refreshValue();
					this._change( null, 0 );
					this._animateOff = false;
					break;
				case "values":
					this._animateOff = true;
					this._refreshValue();
					for ( i = 0; i < valsLength; i += 1 ) {
						this._change( null, i );
					}
					this._animateOff = false;
					break;
				case "min":
				case "max":
					this._animateOff = true;
					this._refreshValue();
					this._animateOff = false;
					break;
				case "range":
					this._animateOff = true;
					this._refresh();
					this._animateOff = false;
					break;
			}
		},

		//internal value getter
		// _value() returns value trimmed by min and max, aligned by step
		_value: function() {
			var val = this.options.value;
			val = this._trimAlignValue( val );

			return val;
		},

		//internal values getter
		// _values() returns array of values trimmed by min and max, aligned by step
		// _values( index ) returns single value trimmed by min and max, aligned by step
		_values: function( index ) {
			var val,
				vals,
				i;

			if ( arguments.length ) {
				val = this.options.values[ index ];
				val = this._trimAlignValue( val );

				return val;
			} else if ( this.options.values && this.options.values.length ) {
				// .slice() creates a copy of the array
				// this copy gets trimmed by min and max and then returned
				vals = this.options.values.slice();
				for ( i = 0; i < vals.length; i+= 1) {
					vals[ i ] = this._trimAlignValue( vals[ i ] );
				}

				return vals;
			} else {
				return [];
			}
		},

		// returns the step-aligned value that val is closest to, between (inclusive) min and max
		_trimAlignValue: function( val ) {
			if ( val <= this._valueMin() ) {
				return this._valueMin();
			}
			if ( val >= this._valueMax() ) {
				return this._valueMax();
			}
			var step = ( this.options.step > 0 ) ? this.options.step : 1,
				valModStep = (val - this._valueMin()) % step,
				alignValue = val - valModStep;

			if ( Math.abs(valModStep) * 2 >= step ) {
				alignValue += ( valModStep > 0 ) ? step : ( -step );
			}

			// Since JavaScript has problems with large floats, round
			// the final value to 5 digits after the decimal point (see #4124)
			return parseFloat( alignValue.toFixed(5) );
		},

		_valueMin: function() {
			return this.options.min;
		},

		_valueMax: function() {
			return this.options.max;
		},

		_refreshValue: function() {
			var lastValPercent, valPercent, value, valueMin, valueMax,
				oRange = this.options.range,
				o = this.options,
				that = this,
				animate = ( !this._animateOff ) ? o.animate : false,
				_set = {};

			if ( this.options.values && this.options.values.length ) {
				this.handles.each(function( i ) {
					valPercent = ( that.values(i) - that._valueMin() ) / ( that._valueMax() - that._valueMin() ) * 100;
					_set[ that.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
					$( this ).stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );
					if ( that.options.range === true ) {
						if ( that.orientation === "horizontal" ) {
							if ( i === 0 ) {
								that.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { left: valPercent + "%" }, o.animate );
							}
							if ( i === 1 ) {
								that.range[ animate ? "animate" : "css" ]( { width: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
							}
						} else {
							if ( i === 0 ) {
								that.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { bottom: ( valPercent ) + "%" }, o.animate );
							}
							if ( i === 1 ) {
								that.range[ animate ? "animate" : "css" ]( { height: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
							}
						}
					}
					lastValPercent = valPercent;
				});
			} else {
				value = this.value();
				valueMin = this._valueMin();
				valueMax = this._valueMax();
				valPercent = ( valueMax !== valueMin ) ?
						( value - valueMin ) / ( valueMax - valueMin ) * 100 :
						0;
				_set[ this.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
				this.handle.stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );

				if ( oRange === "min" && this.orientation === "horizontal" ) {
					this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { width: valPercent + "%" }, o.animate );
				}
				if ( oRange === "max" && this.orientation === "horizontal" ) {
					this.range[ animate ? "animate" : "css" ]( { width: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
				}
				if ( oRange === "min" && this.orientation === "vertical" ) {
					this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { height: valPercent + "%" }, o.animate );
				}
				if ( oRange === "max" && this.orientation === "vertical" ) {
					this.range[ animate ? "animate" : "css" ]( { height: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
				}
			}
		},

		_handleEvents: {
			keydown: function( event ) {
				var allowed, curVal, newVal, step,
					index = $( event.target ).data( "ui-slider-handle-index" );

				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
					case $.ui.keyCode.END:
					case $.ui.keyCode.PAGE_UP:
					case $.ui.keyCode.PAGE_DOWN:
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						event.preventDefault();
						if ( !this._keySliding ) {
							this._keySliding = true;
							$( event.target ).addClass( "ui-state-active" );
							allowed = this._start( event, index );
							if ( allowed === false ) {
								return;
							}
						}
						break;
				}

				step = this.options.step;
				if ( this.options.values && this.options.values.length ) {
					curVal = newVal = this.values( index );
				} else {
					curVal = newVal = this.value();
				}

				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
						newVal = this._valueMin();
						break;
					case $.ui.keyCode.END:
						newVal = this._valueMax();
						break;
					case $.ui.keyCode.PAGE_UP:
						newVal = this._trimAlignValue( curVal + ( (this._valueMax() - this._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.PAGE_DOWN:
						newVal = this._trimAlignValue( curVal - ( (this._valueMax() - this._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
						if ( curVal === this._valueMax() ) {
							return;
						}
						newVal = this._trimAlignValue( curVal + step );
						break;
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						if ( curVal === this._valueMin() ) {
							return;
						}
						newVal = this._trimAlignValue( curVal - step );
						break;
				}

				this._slide( event, index, newVal );
			},
			click: function( event ) {
				event.preventDefault();
			},
			keyup: function( event ) {
				var index = $( event.target ).data( "ui-slider-handle-index" );

				if ( this._keySliding ) {
					this._keySliding = false;
					this._stop( event, index );
					this._change( event, index );
					$( event.target ).removeClass( "ui-state-active" );
				}
			}
		}

	});

	}(jQuery));
	;
	/*!
	 * jQuery UI Accordion 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/accordion/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	var uid = 0,
		hideProps = {},
		showProps = {};

	hideProps.height = hideProps.paddingTop = hideProps.paddingBottom =
		hideProps.borderTopWidth = hideProps.borderBottomWidth = "hide";
	showProps.height = showProps.paddingTop = showProps.paddingBottom =
		showProps.borderTopWidth = showProps.borderBottomWidth = "show";

	$.widget( "ui.accordion", {
		version: "1.10.4",
		options: {
			active: 0,
			animate: {},
			collapsible: false,
			event: "click",
			header: "> li > :first-child,> :not(li):even",
			heightStyle: "auto",
			icons: {
				activeHeader: "ui-icon-triangle-1-s",
				header: "ui-icon-triangle-1-e"
			},

			// callbacks
			activate: null,
			beforeActivate: null
		},

		_create: function() {
			var options = this.options;
			this.prevShow = this.prevHide = $();
			this.element.addClass( "ui-accordion ui-widget ui-helper-reset" )
				// ARIA
				.attr( "role", "tablist" );

			// don't allow collapsible: false and active: false / null
			if ( !options.collapsible && (options.active === false || options.active == null) ) {
				options.active = 0;
			}

			this._processPanels();
			// handle negative values
			if ( options.active < 0 ) {
				options.active += this.headers.length;
			}
			this._refresh();
		},

		_getCreateEventData: function() {
			return {
				header: this.active,
				panel: !this.active.length ? $() : this.active.next(),
				content: !this.active.length ? $() : this.active.next()
			};
		},

		_createIcons: function() {
			var icons = this.options.icons;
			if ( icons ) {
				$( "<span>" )
					.addClass( "ui-accordion-header-icon ui-icon " + icons.header )
					.prependTo( this.headers );
				this.active.children( ".ui-accordion-header-icon" )
					.removeClass( icons.header )
					.addClass( icons.activeHeader );
				this.headers.addClass( "ui-accordion-icons" );
			}
		},

		_destroyIcons: function() {
			this.headers
				.removeClass( "ui-accordion-icons" )
				.children( ".ui-accordion-header-icon" )
					.remove();
		},

		_destroy: function() {
			var contents;

			// clean up main element
			this.element
				.removeClass( "ui-accordion ui-widget ui-helper-reset" )
				.removeAttr( "role" );

			// clean up headers
			this.headers
				.removeClass( "ui-accordion-header ui-accordion-header-active ui-helper-reset ui-state-default ui-corner-all ui-state-active ui-state-disabled ui-corner-top" )
				.removeAttr( "role" )
				.removeAttr( "aria-expanded" )
				.removeAttr( "aria-selected" )
				.removeAttr( "aria-controls" )
				.removeAttr( "tabIndex" )
				.each(function() {
					if ( /^ui-accordion/.test( this.id ) ) {
						this.removeAttribute( "id" );
					}
				});
			this._destroyIcons();

			// clean up content panels
			contents = this.headers.next()
				.css( "display", "" )
				.removeAttr( "role" )
				.removeAttr( "aria-hidden" )
				.removeAttr( "aria-labelledby" )
				.removeClass( "ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content ui-accordion-content-active ui-state-disabled" )
				.each(function() {
					if ( /^ui-accordion/.test( this.id ) ) {
						this.removeAttribute( "id" );
					}
				});
			if ( this.options.heightStyle !== "content" ) {
				contents.css( "height", "" );
			}
		},

		_setOption: function( key, value ) {
			if ( key === "active" ) {
				// _activate() will handle invalid values and update this.options
				this._activate( value );
				return;
			}

			if ( key === "event" ) {
				if ( this.options.event ) {
					this._off( this.headers, this.options.event );
				}
				this._setupEvents( value );
			}

			this._super( key, value );

			// setting collapsible: false while collapsed; open first panel
			if ( key === "collapsible" && !value && this.options.active === false ) {
				this._activate( 0 );
			}

			if ( key === "icons" ) {
				this._destroyIcons();
				if ( value ) {
					this._createIcons();
				}
			}

			// #5332 - opacity doesn't cascade to positioned elements in IE
			// so we need to add the disabled class to the headers and panels
			if ( key === "disabled" ) {
				this.headers.add( this.headers.next() )
					.toggleClass( "ui-state-disabled", !!value );
			}
		},

		_keydown: function( event ) {
			if ( event.altKey || event.ctrlKey ) {
				return;
			}

			var keyCode = $.ui.keyCode,
				length = this.headers.length,
				currentIndex = this.headers.index( event.target ),
				toFocus = false;

			switch ( event.keyCode ) {
				case keyCode.RIGHT:
				case keyCode.DOWN:
					toFocus = this.headers[ ( currentIndex + 1 ) % length ];
					break;
				case keyCode.LEFT:
				case keyCode.UP:
					toFocus = this.headers[ ( currentIndex - 1 + length ) % length ];
					break;
				case keyCode.SPACE:
				case keyCode.ENTER:
					this._eventHandler( event );
					break;
				case keyCode.HOME:
					toFocus = this.headers[ 0 ];
					break;
				case keyCode.END:
					toFocus = this.headers[ length - 1 ];
					break;
			}

			if ( toFocus ) {
				$( event.target ).attr( "tabIndex", -1 );
				$( toFocus ).attr( "tabIndex", 0 );
				toFocus.focus();
				event.preventDefault();
			}
		},

		_panelKeyDown : function( event ) {
			if ( event.keyCode === $.ui.keyCode.UP && event.ctrlKey ) {
				$( event.currentTarget ).prev().focus();
			}
		},

		refresh: function() {
			var options = this.options;
			this._processPanels();

			// was collapsed or no panel
			if ( ( options.active === false && options.collapsible === true ) || !this.headers.length ) {
				options.active = false;
				this.active = $();
			// active false only when collapsible is true
			} else if ( options.active === false ) {
				this._activate( 0 );
			// was active, but active panel is gone
			} else if ( this.active.length && !$.contains( this.element[ 0 ], this.active[ 0 ] ) ) {
				// all remaining panel are disabled
				if ( this.headers.length === this.headers.find(".ui-state-disabled").length ) {
					options.active = false;
					this.active = $();
				// activate previous panel
				} else {
					this._activate( Math.max( 0, options.active - 1 ) );
				}
			// was active, active panel still exists
			} else {
				// make sure active index is correct
				options.active = this.headers.index( this.active );
			}

			this._destroyIcons();

			this._refresh();
		},

		_processPanels: function() {
			this.headers = this.element.find( this.options.header )
				.addClass( "ui-accordion-header ui-helper-reset ui-state-default ui-corner-all" );

			this.headers.next()
				.addClass( "ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom" )
				.filter(":not(.ui-accordion-content-active)")
				.hide();
		},

		_refresh: function() {
			var maxHeight,
				options = this.options,
				heightStyle = options.heightStyle,
				parent = this.element.parent(),
				accordionId = this.accordionId = "ui-accordion-" +
					(this.element.attr( "id" ) || ++uid);

			this.active = this._findActive( options.active )
				.addClass( "ui-accordion-header-active ui-state-active ui-corner-top" )
				.removeClass( "ui-corner-all" );
			this.active.next()
				.addClass( "ui-accordion-content-active" )
				.show();

			this.headers
				.attr( "role", "tab" )
				.each(function( i ) {
					var header = $( this ),
						headerId = header.attr( "id" ),
						panel = header.next(),
						panelId = panel.attr( "id" );
					if ( !headerId ) {
						headerId = accordionId + "-header-" + i;
						header.attr( "id", headerId );
					}
					if ( !panelId ) {
						panelId = accordionId + "-panel-" + i;
						panel.attr( "id", panelId );
					}
					header.attr( "aria-controls", panelId );
					panel.attr( "aria-labelledby", headerId );
				})
				.next()
					.attr( "role", "tabpanel" );

			this.headers
				.not( this.active )
				.attr({
					"aria-selected": "false",
					"aria-expanded": "false",
					tabIndex: -1
				})
				.next()
					.attr({
						"aria-hidden": "true"
					})
					.hide();

			// make sure at least one header is in the tab order
			if ( !this.active.length ) {
				this.headers.eq( 0 ).attr( "tabIndex", 0 );
			} else {
				this.active.attr({
					"aria-selected": "true",
					"aria-expanded": "true",
					tabIndex: 0
				})
				.next()
					.attr({
						"aria-hidden": "false"
					});
			}

			this._createIcons();

			this._setupEvents( options.event );

			if ( heightStyle === "fill" ) {
				maxHeight = parent.height();
				this.element.siblings( ":visible" ).each(function() {
					var elem = $( this ),
						position = elem.css( "position" );

					if ( position === "absolute" || position === "fixed" ) {
						return;
					}
					maxHeight -= elem.outerHeight( true );
				});

				this.headers.each(function() {
					maxHeight -= $( this ).outerHeight( true );
				});

				this.headers.next()
					.each(function() {
						$( this ).height( Math.max( 0, maxHeight -
							$( this ).innerHeight() + $( this ).height() ) );
					})
					.css( "overflow", "auto" );
			} else if ( heightStyle === "auto" ) {
				maxHeight = 0;
				this.headers.next()
					.each(function() {
						maxHeight = Math.max( maxHeight, $( this ).css( "height", "" ).height() );
					})
					.height( maxHeight );
			}
		},

		_activate: function( index ) {
			var active = this._findActive( index )[ 0 ];

			// trying to activate the already active panel
			if ( active === this.active[ 0 ] ) {
				return;
			}

			// trying to collapse, simulate a click on the currently active header
			active = active || this.active[ 0 ];

			this._eventHandler({
				target: active,
				currentTarget: active,
				preventDefault: $.noop
			});
		},

		_findActive: function( selector ) {
			return typeof selector === "number" ? this.headers.eq( selector ) : $();
		},

		_setupEvents: function( event ) {
			var events = {
				keydown: "_keydown"
			};
			if ( event ) {
				$.each( event.split(" "), function( index, eventName ) {
					events[ eventName ] = "_eventHandler";
				});
			}

			this._off( this.headers.add( this.headers.next() ) );
			this._on( this.headers, events );
			this._on( this.headers.next(), { keydown: "_panelKeyDown" });
			this._hoverable( this.headers );
			this._focusable( this.headers );
		},

		_eventHandler: function( event ) {
			var options = this.options,
				active = this.active,
				clicked = $( event.currentTarget ),
				clickedIsActive = clicked[ 0 ] === active[ 0 ],
				collapsing = clickedIsActive && options.collapsible,
				toShow = collapsing ? $() : clicked.next(),
				toHide = active.next(),
				eventData = {
					oldHeader: active,
					oldPanel: toHide,
					newHeader: collapsing ? $() : clicked,
					newPanel: toShow
				};

			event.preventDefault();

			if (
					// click on active header, but not collapsible
					( clickedIsActive && !options.collapsible ) ||
					// allow canceling activation
					( this._trigger( "beforeActivate", event, eventData ) === false ) ) {
				return;
			}

			options.active = collapsing ? false : this.headers.index( clicked );

			// when the call to ._toggle() comes after the class changes
			// it causes a very odd bug in IE 8 (see #6720)
			this.active = clickedIsActive ? $() : clicked;
			this._toggle( eventData );

			// switch classes
			// corner classes on the previously active header stay after the animation
			active.removeClass( "ui-accordion-header-active ui-state-active" );
			if ( options.icons ) {
				active.children( ".ui-accordion-header-icon" )
					.removeClass( options.icons.activeHeader )
					.addClass( options.icons.header );
			}

			if ( !clickedIsActive ) {
				clicked
					.removeClass( "ui-corner-all" )
					.addClass( "ui-accordion-header-active ui-state-active ui-corner-top" );
				if ( options.icons ) {
					clicked.children( ".ui-accordion-header-icon" )
						.removeClass( options.icons.header )
						.addClass( options.icons.activeHeader );
				}

				clicked
					.next()
					.addClass( "ui-accordion-content-active" );
			}
		},

		_toggle: function( data ) {
			var toShow = data.newPanel,
				toHide = this.prevShow.length ? this.prevShow : data.oldPanel;

			// handle activating a panel during the animation for another activation
			this.prevShow.add( this.prevHide ).stop( true, true );
			this.prevShow = toShow;
			this.prevHide = toHide;

			if ( this.options.animate ) {
				this._animate( toShow, toHide, data );
			} else {
				toHide.hide();
				toShow.show();
				this._toggleComplete( data );
			}

			toHide.attr({
				"aria-hidden": "true"
			});
			toHide.prev().attr( "aria-selected", "false" );
			// if we're switching panels, remove the old header from the tab order
			// if we're opening from collapsed state, remove the previous header from the tab order
			// if we're collapsing, then keep the collapsing header in the tab order
			if ( toShow.length && toHide.length ) {
				toHide.prev().attr({
					"tabIndex": -1,
					"aria-expanded": "false"
				});
			} else if ( toShow.length ) {
				this.headers.filter(function() {
					return $( this ).attr( "tabIndex" ) === 0;
				})
				.attr( "tabIndex", -1 );
			}

			toShow
				.attr( "aria-hidden", "false" )
				.prev()
					.attr({
						"aria-selected": "true",
						tabIndex: 0,
						"aria-expanded": "true"
					});
		},

		_animate: function( toShow, toHide, data ) {
			var total, easing, duration,
				that = this,
				adjust = 0,
				down = toShow.length &&
					( !toHide.length || ( toShow.index() < toHide.index() ) ),
				animate = this.options.animate || {},
				options = down && animate.down || animate,
				complete = function() {
					that._toggleComplete( data );
				};

			if ( typeof options === "number" ) {
				duration = options;
			}
			if ( typeof options === "string" ) {
				easing = options;
			}
			// fall back from options to animation in case of partial down settings
			easing = easing || options.easing || animate.easing;
			duration = duration || options.duration || animate.duration;

			if ( !toHide.length ) {
				return toShow.animate( showProps, duration, easing, complete );
			}
			if ( !toShow.length ) {
				return toHide.animate( hideProps, duration, easing, complete );
			}

			total = toShow.show().outerHeight();
			toHide.animate( hideProps, {
				duration: duration,
				easing: easing,
				step: function( now, fx ) {
					fx.now = Math.round( now );
				}
			});
			toShow
				.hide()
				.animate( showProps, {
					duration: duration,
					easing: easing,
					complete: complete,
					step: function( now, fx ) {
						fx.now = Math.round( now );
						if ( fx.prop !== "height" ) {
							adjust += fx.now;
						} else if ( that.options.heightStyle !== "content" ) {
							fx.now = Math.round( total - toHide.outerHeight() - adjust );
							adjust = 0;
						}
					}
				});
		},

		_toggleComplete: function( data ) {
			var toHide = data.oldPanel;

			toHide
				.removeClass( "ui-accordion-content-active" )
				.prev()
					.removeClass( "ui-corner-top" )
					.addClass( "ui-corner-all" );

			// Work around for rendering bug in IE (#5421)
			if ( toHide.length ) {
				toHide.parent()[0].className = toHide.parent()[0].className;
			}
			this._trigger( "activate", null, data );
		}
	});

	})( jQuery );
	;
	/*!
	 * jQuery UI Menu 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/menu/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.widget.js
	 *	jquery.ui.position.js
	 */
	(function( $, undefined ) {

	$.widget( "ui.menu", {
		version: "1.10.4",
		defaultElement: "<ul>",
		delay: 300,
		options: {
			icons: {
				submenu: "ui-icon-carat-1-e"
			},
			menus: "ul",
			position: {
				my: "left top",
				at: "right top"
			},
			role: "menu",

			// callbacks
			blur: null,
			focus: null,
			select: null
		},

		_create: function() {
			this.activeMenu = this.element;
			// flag used to prevent firing of the click handler
			// as the event bubbles up through nested menus
			this.mouseHandled = false;
			this.element
				.uniqueId()
				.addClass( "ui-menu ui-widget ui-widget-content ui-corner-all" )
				.toggleClass( "ui-menu-icons", !!this.element.find( ".ui-icon" ).length )
				.attr({
					role: this.options.role,
					tabIndex: 0
				})
				// need to catch all clicks on disabled menu
				// not possible through _on
				.bind( "click" + this.eventNamespace, $.proxy(function( event ) {
					if ( this.options.disabled ) {
						event.preventDefault();
					}
				}, this ));

			if ( this.options.disabled ) {
				this.element
					.addClass( "ui-state-disabled" )
					.attr( "aria-disabled", "true" );
			}

			this._on({
				// Prevent focus from sticking to links inside menu after clicking
				// them (focus should always stay on UL during navigation).
				"mousedown .ui-menu-item > a": function( event ) {
					event.preventDefault();
				},
				"click .ui-state-disabled > a": function( event ) {
					event.preventDefault();
				},
				"click .ui-menu-item:has(a)": function( event ) {
					var target = $( event.target ).closest( ".ui-menu-item" );
					if ( !this.mouseHandled && target.not( ".ui-state-disabled" ).length ) {
						this.select( event );

						// Only set the mouseHandled flag if the event will bubble, see #9469.
						if ( !event.isPropagationStopped() ) {
							this.mouseHandled = true;
						}

						// Open submenu on click
						if ( target.has( ".ui-menu" ).length ) {
							this.expand( event );
						} else if ( !this.element.is( ":focus" ) && $( this.document[ 0 ].activeElement ).closest( ".ui-menu" ).length ) {

							// Redirect focus to the menu
							this.element.trigger( "focus", [ true ] );

							// If the active item is on the top level, let it stay active.
							// Otherwise, blur the active item since it is no longer visible.
							if ( this.active && this.active.parents( ".ui-menu" ).length === 1 ) {
								clearTimeout( this.timer );
							}
						}
					}
				},
				"mouseenter .ui-menu-item": function( event ) {
					var target = $( event.currentTarget );
					// Remove ui-state-active class from siblings of the newly focused menu item
					// to avoid a jump caused by adjacent elements both having a class with a border
					target.siblings().children( ".ui-state-active" ).removeClass( "ui-state-active" );
					this.focus( event, target );
				},
				mouseleave: "collapseAll",
				"mouseleave .ui-menu": "collapseAll",
				focus: function( event, keepActiveItem ) {
					// If there's already an active item, keep it active
					// If not, activate the first item
					var item = this.active || this.element.children( ".ui-menu-item" ).eq( 0 );

					if ( !keepActiveItem ) {
						this.focus( event, item );
					}
				},
				blur: function( event ) {
					this._delay(function() {
						if ( !$.contains( this.element[0], this.document[0].activeElement ) ) {
							this.collapseAll( event );
						}
					});
				},
				keydown: "_keydown"
			});

			this.refresh();

			// Clicks outside of a menu collapse any open menus
			this._on( this.document, {
				click: function( event ) {
					if ( !$( event.target ).closest( ".ui-menu" ).length ) {
						this.collapseAll( event );
					}

					// Reset the mouseHandled flag
					this.mouseHandled = false;
				}
			});
		},

		_destroy: function() {
			// Destroy (sub)menus
			this.element
				.removeAttr( "aria-activedescendant" )
				.find( ".ui-menu" ).addBack()
					.removeClass( "ui-menu ui-widget ui-widget-content ui-corner-all ui-menu-icons" )
					.removeAttr( "role" )
					.removeAttr( "tabIndex" )
					.removeAttr( "aria-labelledby" )
					.removeAttr( "aria-expanded" )
					.removeAttr( "aria-hidden" )
					.removeAttr( "aria-disabled" )
					.removeUniqueId()
					.show();

			// Destroy menu items
			this.element.find( ".ui-menu-item" )
				.removeClass( "ui-menu-item" )
				.removeAttr( "role" )
				.removeAttr( "aria-disabled" )
				.children( "a" )
					.removeUniqueId()
					.removeClass( "ui-corner-all ui-state-hover" )
					.removeAttr( "tabIndex" )
					.removeAttr( "role" )
					.removeAttr( "aria-haspopup" )
					.children().each( function() {
						var elem = $( this );
						if ( elem.data( "ui-menu-submenu-carat" ) ) {
							elem.remove();
						}
					});

			// Destroy menu dividers
			this.element.find( ".ui-menu-divider" ).removeClass( "ui-menu-divider ui-widget-content" );
		},

		_keydown: function( event ) {
			var match, prev, character, skip, regex,
				preventDefault = true;

			function escape( value ) {
				return value.replace( /[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&" );
			}

			switch ( event.keyCode ) {
			case $.ui.keyCode.PAGE_UP:
				this.previousPage( event );
				break;
			case $.ui.keyCode.PAGE_DOWN:
				this.nextPage( event );
				break;
			case $.ui.keyCode.HOME:
				this._move( "first", "first", event );
				break;
			case $.ui.keyCode.END:
				this._move( "last", "last", event );
				break;
			case $.ui.keyCode.UP:
				this.previous( event );
				break;
			case $.ui.keyCode.DOWN:
				this.next( event );
				break;
			case $.ui.keyCode.LEFT:
				this.collapse( event );
				break;
			case $.ui.keyCode.RIGHT:
				if ( this.active && !this.active.is( ".ui-state-disabled" ) ) {
					this.expand( event );
				}
				break;
			case $.ui.keyCode.ENTER:
			case $.ui.keyCode.SPACE:
				this._activate( event );
				break;
			case $.ui.keyCode.ESCAPE:
				this.collapse( event );
				break;
			default:
				preventDefault = false;
				prev = this.previousFilter || "";
				character = String.fromCharCode( event.keyCode );
				skip = false;

				clearTimeout( this.filterTimer );

				if ( character === prev ) {
					skip = true;
				} else {
					character = prev + character;
				}

				regex = new RegExp( "^" + escape( character ), "i" );
				match = this.activeMenu.children( ".ui-menu-item" ).filter(function() {
					return regex.test( $( this ).children( "a" ).text() );
				});
				match = skip && match.index( this.active.next() ) !== -1 ?
					this.active.nextAll( ".ui-menu-item" ) :
					match;

				// If no matches on the current filter, reset to the last character pressed
				// to move down the menu to the first item that starts with that character
				if ( !match.length ) {
					character = String.fromCharCode( event.keyCode );
					regex = new RegExp( "^" + escape( character ), "i" );
					match = this.activeMenu.children( ".ui-menu-item" ).filter(function() {
						return regex.test( $( this ).children( "a" ).text() );
					});
				}

				if ( match.length ) {
					this.focus( event, match );
					if ( match.length > 1 ) {
						this.previousFilter = character;
						this.filterTimer = this._delay(function() {
							delete this.previousFilter;
						}, 1000 );
					} else {
						delete this.previousFilter;
					}
				} else {
					delete this.previousFilter;
				}
			}

			if ( preventDefault ) {
				event.preventDefault();
			}
		},

		_activate: function( event ) {
			if ( !this.active.is( ".ui-state-disabled" ) ) {
				if ( this.active.children( "a[aria-haspopup='true']" ).length ) {
					this.expand( event );
				} else {
					this.select( event );
				}
			}
		},

		refresh: function() {
			var menus,
				icon = this.options.icons.submenu,
				submenus = this.element.find( this.options.menus );

			this.element.toggleClass( "ui-menu-icons", !!this.element.find( ".ui-icon" ).length );

			// Initialize nested menus
			submenus.filter( ":not(.ui-menu)" )
				.addClass( "ui-menu ui-widget ui-widget-content ui-corner-all" )
				.hide()
				.attr({
					role: this.options.role,
					"aria-hidden": "true",
					"aria-expanded": "false"
				})
				.each(function() {
					var menu = $( this ),
						item = menu.prev( "a" ),
						submenuCarat = $( "<span>" )
							.addClass( "ui-menu-icon ui-icon " + icon )
							.data( "ui-menu-submenu-carat", true );

					item
						.attr( "aria-haspopup", "true" )
						.prepend( submenuCarat );
					menu.attr( "aria-labelledby", item.attr( "id" ) );
				});

			menus = submenus.add( this.element );

			// Don't refresh list items that are already adapted
			menus.children( ":not(.ui-menu-item):has(a)" )
				.addClass( "ui-menu-item" )
				.attr( "role", "presentation" )
				.children( "a" )
					.uniqueId()
					.addClass( "ui-corner-all" )
					.attr({
						tabIndex: -1,
						role: this._itemRole()
					});

			// Initialize unlinked menu-items containing spaces and/or dashes only as dividers
			menus.children( ":not(.ui-menu-item)" ).each(function() {
				var item = $( this );
				// hyphen, em dash, en dash
				if ( !/[^\-\u2014\u2013\s]/.test( item.text() ) ) {
					item.addClass( "ui-widget-content ui-menu-divider" );
				}
			});

			// Add aria-disabled attribute to any disabled menu item
			menus.children( ".ui-state-disabled" ).attr( "aria-disabled", "true" );

			// If the active item has been removed, blur the menu
			if ( this.active && !$.contains( this.element[ 0 ], this.active[ 0 ] ) ) {
				this.blur();
			}
		},

		_itemRole: function() {
			return {
				menu: "menuitem",
				listbox: "option"
			}[ this.options.role ];
		},

		_setOption: function( key, value ) {
			if ( key === "icons" ) {
				this.element.find( ".ui-menu-icon" )
					.removeClass( this.options.icons.submenu )
					.addClass( value.submenu );
			}
			this._super( key, value );
		},

		focus: function( event, item ) {
			var nested, focused;
			this.blur( event, event && event.type === "focus" );

			this._scrollIntoView( item );

			this.active = item.first();
			focused = this.active.children( "a" ).addClass( "ui-state-focus" );
			// Only update aria-activedescendant if there's a role
			// otherwise we assume focus is managed elsewhere
			if ( this.options.role ) {
				this.element.attr( "aria-activedescendant", focused.attr( "id" ) );
			}

			// Highlight active parent menu item, if any
			this.active
				.parent()
				.closest( ".ui-menu-item" )
				.children( "a:first" )
				.addClass( "ui-state-active" );

			if ( event && event.type === "keydown" ) {
				this._close();
			} else {
				this.timer = this._delay(function() {
					this._close();
				}, this.delay );
			}

			nested = item.children( ".ui-menu" );
			if ( nested.length && event && ( /^mouse/.test( event.type ) ) ) {
				this._startOpening(nested);
			}
			this.activeMenu = item.parent();

			this._trigger( "focus", event, { item: item } );
		},

		_scrollIntoView: function( item ) {
			var borderTop, paddingTop, offset, scroll, elementHeight, itemHeight;
			if ( this._hasScroll() ) {
				borderTop = parseFloat( $.css( this.activeMenu[0], "borderTopWidth" ) ) || 0;
				paddingTop = parseFloat( $.css( this.activeMenu[0], "paddingTop" ) ) || 0;
				offset = item.offset().top - this.activeMenu.offset().top - borderTop - paddingTop;
				scroll = this.activeMenu.scrollTop();
				elementHeight = this.activeMenu.height();
				itemHeight = item.height();

				if ( offset < 0 ) {
					this.activeMenu.scrollTop( scroll + offset );
				} else if ( offset + itemHeight > elementHeight ) {
					this.activeMenu.scrollTop( scroll + offset - elementHeight + itemHeight );
				}
			}
		},

		blur: function( event, fromFocus ) {
			if ( !fromFocus ) {
				clearTimeout( this.timer );
			}

			if ( !this.active ) {
				return;
			}

			this.active.children( "a" ).removeClass( "ui-state-focus" );
			this.active = null;

			this._trigger( "blur", event, { item: this.active } );
		},

		_startOpening: function( submenu ) {
			clearTimeout( this.timer );

			// Don't open if already open fixes a Firefox bug that caused a .5 pixel
			// shift in the submenu position when mousing over the carat icon
			if ( submenu.attr( "aria-hidden" ) !== "true" ) {
				return;
			}

			this.timer = this._delay(function() {
				this._close();
				this._open( submenu );
			}, this.delay );
		},

		_open: function( submenu ) {
			var position = $.extend({
				of: this.active
			}, this.options.position );

			clearTimeout( this.timer );
			this.element.find( ".ui-menu" ).not( submenu.parents( ".ui-menu" ) )
				.hide()
				.attr( "aria-hidden", "true" );

			submenu
				.show()
				.removeAttr( "aria-hidden" )
				.attr( "aria-expanded", "true" )
				.position( position );
		},

		collapseAll: function( event, all ) {
			clearTimeout( this.timer );
			this.timer = this._delay(function() {
				// If we were passed an event, look for the submenu that contains the event
				var currentMenu = all ? this.element :
					$( event && event.target ).closest( this.element.find( ".ui-menu" ) );

				// If we found no valid submenu ancestor, use the main menu to close all sub menus anyway
				if ( !currentMenu.length ) {
					currentMenu = this.element;
				}

				this._close( currentMenu );

				this.blur( event );
				this.activeMenu = currentMenu;
			}, this.delay );
		},

		// With no arguments, closes the currently active menu - if nothing is active
		// it closes all menus.  If passed an argument, it will search for menus BELOW
		_close: function( startMenu ) {
			if ( !startMenu ) {
				startMenu = this.active ? this.active.parent() : this.element;
			}

			startMenu
				.find( ".ui-menu" )
					.hide()
					.attr( "aria-hidden", "true" )
					.attr( "aria-expanded", "false" )
				.end()
				.find( "a.ui-state-active" )
					.removeClass( "ui-state-active" );
		},

		collapse: function( event ) {
			var newItem = this.active &&
				this.active.parent().closest( ".ui-menu-item", this.element );
			if ( newItem && newItem.length ) {
				this._close();
				this.focus( event, newItem );
			}
		},

		expand: function( event ) {
			var newItem = this.active &&
				this.active
					.children( ".ui-menu " )
					.children( ".ui-menu-item" )
					.first();

			if ( newItem && newItem.length ) {
				this._open( newItem.parent() );

				// Delay so Firefox will not hide activedescendant change in expanding submenu from AT
				this._delay(function() {
					this.focus( event, newItem );
				});
			}
		},

		next: function( event ) {
			this._move( "next", "first", event );
		},

		previous: function( event ) {
			this._move( "prev", "last", event );
		},

		isFirstItem: function() {
			return this.active && !this.active.prevAll( ".ui-menu-item" ).length;
		},

		isLastItem: function() {
			return this.active && !this.active.nextAll( ".ui-menu-item" ).length;
		},

		_move: function( direction, filter, event ) {
			var next;
			if ( this.active ) {
				if ( direction === "first" || direction === "last" ) {
					next = this.active
						[ direction === "first" ? "prevAll" : "nextAll" ]( ".ui-menu-item" )
						.eq( -1 );
				} else {
					next = this.active
						[ direction + "All" ]( ".ui-menu-item" )
						.eq( 0 );
				}
			}
			if ( !next || !next.length || !this.active ) {
				next = this.activeMenu.children( ".ui-menu-item" )[ filter ]();
			}

			this.focus( event, next );
		},

		nextPage: function( event ) {
			var item, base, height;

			if ( !this.active ) {
				this.next( event );
				return;
			}
			if ( this.isLastItem() ) {
				return;
			}
			if ( this._hasScroll() ) {
				base = this.active.offset().top;
				height = this.element.height();
				this.active.nextAll( ".ui-menu-item" ).each(function() {
					item = $( this );
					return item.offset().top - base - height < 0;
				});

				this.focus( event, item );
			} else {
				this.focus( event, this.activeMenu.children( ".ui-menu-item" )
					[ !this.active ? "first" : "last" ]() );
			}
		},

		previousPage: function( event ) {
			var item, base, height;
			if ( !this.active ) {
				this.next( event );
				return;
			}
			if ( this.isFirstItem() ) {
				return;
			}
			if ( this._hasScroll() ) {
				base = this.active.offset().top;
				height = this.element.height();
				this.active.prevAll( ".ui-menu-item" ).each(function() {
					item = $( this );
					return item.offset().top - base + height > 0;
				});

				this.focus( event, item );
			} else {
				this.focus( event, this.activeMenu.children( ".ui-menu-item" ).first() );
			}
		},

		_hasScroll: function() {
			return this.element.outerHeight() < this.element.prop( "scrollHeight" );
		},

		select: function( event ) {
			// TODO: It should never be possible to not have an active item at this
			// point, but the tests don't trigger mouseenter before click.
			this.active = this.active || $( event.target ).closest( ".ui-menu-item" );
			var ui = { item: this.active };
			if ( !this.active.has( ".ui-menu" ).length ) {
				this.collapseAll( event, true );
			}
			this._trigger( "select", event, ui );
		}
	});

	}( jQuery ));
	;
	/*!
	 * jQuery UI Autocomplete 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/autocomplete/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.widget.js
	 *	jquery.ui.position.js
	 *	jquery.ui.menu.js
	 */
	(function( $, undefined ) {

	$.widget( "ui.autocomplete", {
		version: "1.10.4",
		defaultElement: "<input>",
		options: {
			appendTo: null,
			autoFocus: false,
			delay: 300,
			minLength: 1,
			position: {
				my: "left top",
				at: "left bottom",
				collision: "none"
			},
			source: null,

			// callbacks
			change: null,
			close: null,
			focus: null,
			open: null,
			response: null,
			search: null,
			select: null
		},

		requestIndex: 0,
		pending: 0,

		_create: function() {
			// Some browsers only repeat keydown events, not keypress events,
			// so we use the suppressKeyPress flag to determine if we've already
			// handled the keydown event. #7269
			// Unfortunately the code for & in keypress is the same as the up arrow,
			// so we use the suppressKeyPressRepeat flag to avoid handling keypress
			// events when we know the keydown event was used to modify the
			// search term. #7799
			var suppressKeyPress, suppressKeyPressRepeat, suppressInput,
				nodeName = this.element[0].nodeName.toLowerCase(),
				isTextarea = nodeName === "textarea",
				isInput = nodeName === "input";

			this.isMultiLine =
				// Textareas are always multi-line
				isTextarea ? true :
				// Inputs are always single-line, even if inside a contentEditable element
				// IE also treats inputs as contentEditable
				isInput ? false :
				// All other element types are determined by whether or not they're contentEditable
				this.element.prop( "isContentEditable" );

			this.valueMethod = this.element[ isTextarea || isInput ? "val" : "text" ];
			this.isNewMenu = true;

			this.element
				.addClass( "ui-autocomplete-input" )
				.attr( "autocomplete", "off" );

			this._on( this.element, {
				keydown: function( event ) {
					if ( this.element.prop( "readOnly" ) ) {
						suppressKeyPress = true;
						suppressInput = true;
						suppressKeyPressRepeat = true;
						return;
					}

					suppressKeyPress = false;
					suppressInput = false;
					suppressKeyPressRepeat = false;
					var keyCode = $.ui.keyCode;
					switch( event.keyCode ) {
					case keyCode.PAGE_UP:
						suppressKeyPress = true;
						this._move( "previousPage", event );
						break;
					case keyCode.PAGE_DOWN:
						suppressKeyPress = true;
						this._move( "nextPage", event );
						break;
					case keyCode.UP:
						suppressKeyPress = true;
						this._keyEvent( "previous", event );
						break;
					case keyCode.DOWN:
						suppressKeyPress = true;
						this._keyEvent( "next", event );
						break;
					case keyCode.ENTER:
					case keyCode.NUMPAD_ENTER:
						// when menu is open and has focus
						if ( this.menu.active ) {
							// #6055 - Opera still allows the keypress to occur
							// which causes forms to submit
							suppressKeyPress = true;
							event.preventDefault();
							this.menu.select( event );
						}
						break;
					case keyCode.TAB:
						if ( this.menu.active ) {
							this.menu.select( event );
						}
						break;
					case keyCode.ESCAPE:
						if ( this.menu.element.is( ":visible" ) ) {
							this._value( this.term );
							this.close( event );
							// Different browsers have different default behavior for escape
							// Single press can mean undo or clear
							// Double press in IE means clear the whole form
							event.preventDefault();
						}
						break;
					default:
						suppressKeyPressRepeat = true;
						// search timeout should be triggered before the input value is changed
						this._searchTimeout( event );
						break;
					}
				},
				keypress: function( event ) {
					if ( suppressKeyPress ) {
						suppressKeyPress = false;
						if ( !this.isMultiLine || this.menu.element.is( ":visible" ) ) {
							event.preventDefault();
						}
						return;
					}
					if ( suppressKeyPressRepeat ) {
						return;
					}

					// replicate some key handlers to allow them to repeat in Firefox and Opera
					var keyCode = $.ui.keyCode;
					switch( event.keyCode ) {
					case keyCode.PAGE_UP:
						this._move( "previousPage", event );
						break;
					case keyCode.PAGE_DOWN:
						this._move( "nextPage", event );
						break;
					case keyCode.UP:
						this._keyEvent( "previous", event );
						break;
					case keyCode.DOWN:
						this._keyEvent( "next", event );
						break;
					}
				},
				input: function( event ) {
					if ( suppressInput ) {
						suppressInput = false;
						event.preventDefault();
						return;
					}
					this._searchTimeout( event );
				},
				focus: function() {
					this.selectedItem = null;
					this.previous = this._value();
				},
				blur: function( event ) {
					if ( this.cancelBlur ) {
						delete this.cancelBlur;
						return;
					}

					clearTimeout( this.searching );
					this.close( event );
					this._change( event );
				}
			});

			this._initSource();
			this.menu = $( "<ul>" )
				.addClass( "ui-autocomplete ui-front" )
				.appendTo( this._appendTo() )
				.menu({
					// disable ARIA support, the live region takes care of that
					role: null
				})
				.hide()
				.data( "ui-menu" );

			this._on( this.menu.element, {
				mousedown: function( event ) {
					// prevent moving focus out of the text field
					event.preventDefault();

					// IE doesn't prevent moving focus even with event.preventDefault()
					// so we set a flag to know when we should ignore the blur event
					this.cancelBlur = true;
					this._delay(function() {
						delete this.cancelBlur;
					});

					// clicking on the scrollbar causes focus to shift to the body
					// but we can't detect a mouseup or a click immediately afterward
					// so we have to track the next mousedown and close the menu if
					// the user clicks somewhere outside of the autocomplete
					var menuElement = this.menu.element[ 0 ];
					if ( !$( event.target ).closest( ".ui-menu-item" ).length ) {
						this._delay(function() {
							var that = this;
							this.document.one( "mousedown", function( event ) {
								if ( event.target !== that.element[ 0 ] &&
										event.target !== menuElement &&
										!$.contains( menuElement, event.target ) ) {
									that.close();
								}
							});
						});
					}
				},
				menufocus: function( event, ui ) {
					// support: Firefox
					// Prevent accidental activation of menu items in Firefox (#7024 #9118)
					if ( this.isNewMenu ) {
						this.isNewMenu = false;
						if ( event.originalEvent && /^mouse/.test( event.originalEvent.type ) ) {
							this.menu.blur();

							this.document.one( "mousemove", function() {
								$( event.target ).trigger( event.originalEvent );
							});

							return;
						}
					}

					var item = ui.item.data( "ui-autocomplete-item" );
					if ( false !== this._trigger( "focus", event, { item: item } ) ) {
						// use value to match what will end up in the input, if it was a key event
						if ( event.originalEvent && /^key/.test( event.originalEvent.type ) ) {
							this._value( item.value );
						}
					} else {
						// Normally the input is populated with the item's value as the
						// menu is navigated, causing screen readers to notice a change and
						// announce the item. Since the focus event was canceled, this doesn't
						// happen, so we update the live region so that screen readers can
						// still notice the change and announce it.
						this.liveRegion.text( item.value );
					}
				},
				menuselect: function( event, ui ) {
					var item = ui.item.data( "ui-autocomplete-item" ),
						previous = this.previous;

					// only trigger when focus was lost (click on menu)
					if ( this.element[0] !== this.document[0].activeElement ) {
						this.element.focus();
						this.previous = previous;
						// #6109 - IE triggers two focus events and the second
						// is asynchronous, so we need to reset the previous
						// term synchronously and asynchronously :-(
						this._delay(function() {
							this.previous = previous;
							this.selectedItem = item;
						});
					}

					if ( false !== this._trigger( "select", event, { item: item } ) ) {
						this._value( item.value );
					}
					// reset the term after the select event
					// this allows custom select handling to work properly
					this.term = this._value();

					this.close( event );
					this.selectedItem = item;
				}
			});

			this.liveRegion = $( "<span>", {
					role: "status",
					"aria-live": "polite"
				})
				.addClass( "ui-helper-hidden-accessible" )
				.insertBefore( this.element );

			// turning off autocomplete prevents the browser from remembering the
			// value when navigating through history, so we re-enable autocomplete
			// if the page is unloaded before the widget is destroyed. #7790
			this._on( this.window, {
				beforeunload: function() {
					this.element.removeAttr( "autocomplete" );
				}
			});
		},

		_destroy: function() {
			clearTimeout( this.searching );
			this.element
				.removeClass( "ui-autocomplete-input" )
				.removeAttr( "autocomplete" );
			this.menu.element.remove();
			this.liveRegion.remove();
		},

		_setOption: function( key, value ) {
			this._super( key, value );
			if ( key === "source" ) {
				this._initSource();
			}
			if ( key === "appendTo" ) {
				this.menu.element.appendTo( this._appendTo() );
			}
			if ( key === "disabled" && value && this.xhr ) {
				this.xhr.abort();
			}
		},

		_appendTo: function() {
			var element = this.options.appendTo;

			if ( element ) {
				element = element.jquery || element.nodeType ?
					$( element ) :
					this.document.find( element ).eq( 0 );
			}

			if ( !element ) {
				element = this.element.closest( ".ui-front" );
			}

			if ( !element.length ) {
				element = this.document[0].body;
			}

			return element;
		},

		_initSource: function() {
			var array, url,
				that = this;
			if ( $.isArray(this.options.source) ) {
				array = this.options.source;
				this.source = function( request, response ) {
					response( $.ui.autocomplete.filter( array, request.term ) );
				};
			} else if ( typeof this.options.source === "string" ) {
				url = this.options.source;
				this.source = function( request, response ) {
					if ( that.xhr ) {
						that.xhr.abort();
					}
					that.xhr = $.ajax({
						url: url,
						data: request,
						dataType: "json",
						success: function( data ) {
							response( data );
						},
						error: function() {
							response( [] );
						}
					});
				};
			} else {
				this.source = this.options.source;
			}
		},

		_searchTimeout: function( event ) {
			clearTimeout( this.searching );
			this.searching = this._delay(function() {
				// only search if the value has changed
				if ( this.term !== this._value() ) {
					this.selectedItem = null;
					this.search( null, event );
				}
			}, this.options.delay );
		},

		search: function( value, event ) {
			value = value != null ? value : this._value();

			// always save the actual value, not the one passed as an argument
			this.term = this._value();

			if ( value.length < this.options.minLength ) {
				return this.close( event );
			}

			if ( this._trigger( "search", event ) === false ) {
				return;
			}

			return this._search( value );
		},

		_search: function( value ) {
			this.pending++;
			this.element.addClass( "ui-autocomplete-loading" );
			this.cancelSearch = false;

			this.source( { term: value }, this._response() );
		},

		_response: function() {
			var index = ++this.requestIndex;

			return $.proxy(function( content ) {
				if ( index === this.requestIndex ) {
					this.__response( content );
				}

				this.pending--;
				if ( !this.pending ) {
					this.element.removeClass( "ui-autocomplete-loading" );
				}
			}, this );
		},

		__response: function( content ) {
			if ( content ) {
				content = this._normalize( content );
			}
			this._trigger( "response", null, { content: content } );
			if ( !this.options.disabled && content && content.length && !this.cancelSearch ) {
				this._suggest( content );
				this._trigger( "open" );
			} else {
				// use ._close() instead of .close() so we don't cancel future searches
				this._close();
			}
		},

		close: function( event ) {
			this.cancelSearch = true;
			this._close( event );
		},

		_close: function( event ) {
			if ( this.menu.element.is( ":visible" ) ) {
				this.menu.element.hide();
				this.menu.blur();
				this.isNewMenu = true;
				this._trigger( "close", event );
			}
		},

		_change: function( event ) {
			if ( this.previous !== this._value() ) {
				this._trigger( "change", event, { item: this.selectedItem } );
			}
		},

		_normalize: function( items ) {
			// assume all items have the right format when the first item is complete
			if ( items.length && items[0].label && items[0].value ) {
				return items;
			}
			return $.map( items, function( item ) {
				if ( typeof item === "string" ) {
					return {
						label: item,
						value: item
					};
				}
				return $.extend({
					label: item.label || item.value,
					value: item.value || item.label
				}, item );
			});
		},

		_suggest: function( items ) {
			var ul = this.menu.element.empty();
			this._renderMenu( ul, items );
			this.isNewMenu = true;
			this.menu.refresh();

			// size and position menu
			ul.show();
			this._resizeMenu();
			ul.position( $.extend({
				of: this.element
			}, this.options.position ));

			if ( this.options.autoFocus ) {
				this.menu.next();
			}
		},

		_resizeMenu: function() {
			var ul = this.menu.element;
			ul.outerWidth( Math.max(
				// Firefox wraps long text (possibly a rounding bug)
				// so we add 1px to avoid the wrapping (#7513)
				ul.width( "" ).outerWidth() + 1,
				this.element.outerWidth()
			) );
		},

		_renderMenu: function( ul, items ) {
			var that = this;
			$.each( items, function( index, item ) {
				that._renderItemData( ul, item );
			});
		},

		_renderItemData: function( ul, item ) {
			return this._renderItem( ul, item ).data( "ui-autocomplete-item", item );
		},

		_renderItem: function( ul, item ) {
			return $( "<li>" )
				.append( $( "<a>" ).text( item.label ) )
				.appendTo( ul );
		},

		_move: function( direction, event ) {
			if ( !this.menu.element.is( ":visible" ) ) {
				this.search( null, event );
				return;
			}
			if ( this.menu.isFirstItem() && /^previous/.test( direction ) ||
					this.menu.isLastItem() && /^next/.test( direction ) ) {
				this._value( this.term );
				this.menu.blur();
				return;
			}
			this.menu[ direction ]( event );
		},

		widget: function() {
			return this.menu.element;
		},

		_value: function() {
			return this.valueMethod.apply( this.element, arguments );
		},

		_keyEvent: function( keyEvent, event ) {
			if ( !this.isMultiLine || this.menu.element.is( ":visible" ) ) {
				this._move( keyEvent, event );

				// prevents moving cursor to beginning/end of the text field in some browsers
				event.preventDefault();
			}
		}
	});

	$.extend( $.ui.autocomplete, {
		escapeRegex: function( value ) {
			return value.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&");
		},
		filter: function(array, term) {
			var matcher = new RegExp( $.ui.autocomplete.escapeRegex(term), "i" );
			return $.grep( array, function(value) {
				return matcher.test( value.label || value.value || value );
			});
		}
	});


	// live region extension, adding a `messages` option
	// NOTE: This is an experimental API. We are still investigating
	// a full solution for string manipulation and internationalization.
	$.widget( "ui.autocomplete", $.ui.autocomplete, {
		options: {
			messages: {
				noResults: "No search results.",
				results: function( amount ) {
					return amount + ( amount > 1 ? " results are" : " result is" ) +
						" available, use up and down arrow keys to navigate.";
				}
			}
		},

		__response: function( content ) {
			var message;
			this._superApply( arguments );
			if ( this.options.disabled || this.cancelSearch ) {
				return;
			}
			if ( content && content.length ) {
				message = this.options.messages.results( content.length );
			} else {
				message = this.options.messages.noResults;
			}
			this.liveRegion.text( message );
		}
	});

	}( jQuery ));
	;
	/*!
	 * jQuery UI Spinner 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/spinner/
	 *
	 * Depends:
	 *  jquery.ui.core.js
	 *  jquery.ui.widget.js
	 *  jquery.ui.button.js
	 */
	(function( $ ) {

	function modifier( fn ) {
		return function() {
			var previous = this.element.val();
			fn.apply( this, arguments );
			this._refresh();
			if ( previous !== this.element.val() ) {
				this._trigger( "change" );
			}
		};
	}

	$.widget( "ui.spinner", {
		version: "1.10.4",
		defaultElement: "<input>",
		widgetEventPrefix: "spin",
		options: {
			culture: null,
			icons: {
				down: "ui-icon-triangle-1-s",
				up: "ui-icon-triangle-1-n"
			},
			incremental: true,
			max: null,
			min: null,
			numberFormat: null,
			page: 10,
			step: 1,

			change: null,
			spin: null,
			start: null,
			stop: null
		},

		_create: function() {
			// handle string values that need to be parsed
			this._setOption( "max", this.options.max );
			this._setOption( "min", this.options.min );
			this._setOption( "step", this.options.step );

			// Only format if there is a value, prevents the field from being marked
			// as invalid in Firefox, see #9573.
			if ( this.value() !== "" ) {
				// Format the value, but don't constrain.
				this._value( this.element.val(), true );
			}

			this._draw();
			this._on( this._events );
			this._refresh();

			// turning off autocomplete prevents the browser from remembering the
			// value when navigating through history, so we re-enable autocomplete
			// if the page is unloaded before the widget is destroyed. #7790
			this._on( this.window, {
				beforeunload: function() {
					this.element.removeAttr( "autocomplete" );
				}
			});
		},

		_getCreateOptions: function() {
			var options = {},
				element = this.element;

			$.each( [ "min", "max", "step" ], function( i, option ) {
				var value = element.attr( option );
				if ( value !== undefined && value.length ) {
					options[ option ] = value;
				}
			});

			return options;
		},

		_events: {
			keydown: function( event ) {
				if ( this._start( event ) && this._keydown( event ) ) {
					event.preventDefault();
				}
			},
			keyup: "_stop",
			focus: function() {
				this.previous = this.element.val();
			},
			blur: function( event ) {
				if ( this.cancelBlur ) {
					delete this.cancelBlur;
					return;
				}

				this._stop();
				this._refresh();
				if ( this.previous !== this.element.val() ) {
					this._trigger( "change", event );
				}
			},
			mousewheel: function( event, delta ) {
				if ( !delta ) {
					return;
				}
				if ( !this.spinning && !this._start( event ) ) {
					return false;
				}

				this._spin( (delta > 0 ? 1 : -1) * this.options.step, event );
				clearTimeout( this.mousewheelTimer );
				this.mousewheelTimer = this._delay(function() {
					if ( this.spinning ) {
						this._stop( event );
					}
				}, 100 );
				event.preventDefault();
			},
			"mousedown .ui-spinner-button": function( event ) {
				var previous;

				// We never want the buttons to have focus; whenever the user is
				// interacting with the spinner, the focus should be on the input.
				// If the input is focused then this.previous is properly set from
				// when the input first received focus. If the input is not focused
				// then we need to set this.previous based on the value before spinning.
				previous = this.element[0] === this.document[0].activeElement ?
					this.previous : this.element.val();
				function checkFocus() {
					var isActive = this.element[0] === this.document[0].activeElement;
					if ( !isActive ) {
						this.element.focus();
						this.previous = previous;
						// support: IE
						// IE sets focus asynchronously, so we need to check if focus
						// moved off of the input because the user clicked on the button.
						this._delay(function() {
							this.previous = previous;
						});
					}
				}

				// ensure focus is on (or stays on) the text field
				event.preventDefault();
				checkFocus.call( this );

				// support: IE
				// IE doesn't prevent moving focus even with event.preventDefault()
				// so we set a flag to know when we should ignore the blur event
				// and check (again) if focus moved off of the input.
				this.cancelBlur = true;
				this._delay(function() {
					delete this.cancelBlur;
					checkFocus.call( this );
				});

				if ( this._start( event ) === false ) {
					return;
				}

				this._repeat( null, $( event.currentTarget ).hasClass( "ui-spinner-up" ) ? 1 : -1, event );
			},
			"mouseup .ui-spinner-button": "_stop",
			"mouseenter .ui-spinner-button": function( event ) {
				// button will add ui-state-active if mouse was down while mouseleave and kept down
				if ( !$( event.currentTarget ).hasClass( "ui-state-active" ) ) {
					return;
				}

				if ( this._start( event ) === false ) {
					return false;
				}
				this._repeat( null, $( event.currentTarget ).hasClass( "ui-spinner-up" ) ? 1 : -1, event );
			},
			// TODO: do we really want to consider this a stop?
			// shouldn't we just stop the repeater and wait until mouseup before
			// we trigger the stop event?
			"mouseleave .ui-spinner-button": "_stop"
		},

		_draw: function() {
			var uiSpinner = this.uiSpinner = this.element
				.addClass( "ui-spinner-input" )
				.attr( "autocomplete", "off" )
				.wrap( this._uiSpinnerHtml() )
				.parent()
					// add buttons
					.append( this._buttonHtml() );

			this.element.attr( "role", "spinbutton" );

			// button bindings
			this.buttons = uiSpinner.find( ".ui-spinner-button" )
				.attr( "tabIndex", -1 )
				.button()
				.removeClass( "ui-corner-all" );

			// IE 6 doesn't understand height: 50% for the buttons
			// unless the wrapper has an explicit height
			if ( this.buttons.height() > Math.ceil( uiSpinner.height() * 0.5 ) &&
					uiSpinner.height() > 0 ) {
				uiSpinner.height( uiSpinner.height() );
			}

			// disable spinner if element was already disabled
			if ( this.options.disabled ) {
				this.disable();
			}
		},

		_keydown: function( event ) {
			var options = this.options,
				keyCode = $.ui.keyCode;

			switch ( event.keyCode ) {
			case keyCode.UP:
				this._repeat( null, 1, event );
				return true;
			case keyCode.DOWN:
				this._repeat( null, -1, event );
				return true;
			case keyCode.PAGE_UP:
				this._repeat( null, options.page, event );
				return true;
			case keyCode.PAGE_DOWN:
				this._repeat( null, -options.page, event );
				return true;
			}

			return false;
		},

		_uiSpinnerHtml: function() {
			return "<span class='ui-spinner ui-widget ui-widget-content ui-corner-all'></span>";
		},

		_buttonHtml: function() {
			return "" +
				"<a class='ui-spinner-button ui-spinner-up ui-corner-tr'>" +
					"<span class='ui-icon " + this.options.icons.up + "'>&#9650;</span>" +
				"</a>" +
				"<a class='ui-spinner-button ui-spinner-down ui-corner-br'>" +
					"<span class='ui-icon " + this.options.icons.down + "'>&#9660;</span>" +
				"</a>";
		},

		_start: function( event ) {
			if ( !this.spinning && this._trigger( "start", event ) === false ) {
				return false;
			}

			if ( !this.counter ) {
				this.counter = 1;
			}
			this.spinning = true;
			return true;
		},

		_repeat: function( i, steps, event ) {
			i = i || 500;

			clearTimeout( this.timer );
			this.timer = this._delay(function() {
				this._repeat( 40, steps, event );
			}, i );

			this._spin( steps * this.options.step, event );
		},

		_spin: function( step, event ) {
			var value = this.value() || 0;

			if ( !this.counter ) {
				this.counter = 1;
			}

			value = this._adjustValue( value + step * this._increment( this.counter ) );

			if ( !this.spinning || this._trigger( "spin", event, { value: value } ) !== false) {
				this._value( value );
				this.counter++;
			}
		},

		_increment: function( i ) {
			var incremental = this.options.incremental;

			if ( incremental ) {
				return $.isFunction( incremental ) ?
					incremental( i ) :
					Math.floor( i*i*i/50000 - i*i/500 + 17*i/200 + 1 );
			}

			return 1;
		},

		_precision: function() {
			var precision = this._precisionOf( this.options.step );
			if ( this.options.min !== null ) {
				precision = Math.max( precision, this._precisionOf( this.options.min ) );
			}
			return precision;
		},

		_precisionOf: function( num ) {
			var str = num.toString(),
				decimal = str.indexOf( "." );
			return decimal === -1 ? 0 : str.length - decimal - 1;
		},

		_adjustValue: function( value ) {
			var base, aboveMin,
				options = this.options;

			// make sure we're at a valid step
			// - find out where we are relative to the base (min or 0)
			base = options.min !== null ? options.min : 0;
			aboveMin = value - base;
			// - round to the nearest step
			aboveMin = Math.round(aboveMin / options.step) * options.step;
			// - rounding is based on 0, so adjust back to our base
			value = base + aboveMin;

			// fix precision from bad JS floating point math
			value = parseFloat( value.toFixed( this._precision() ) );

			// clamp the value
			if ( options.max !== null && value > options.max) {
				return options.max;
			}
			if ( options.min !== null && value < options.min ) {
				return options.min;
			}

			return value;
		},

		_stop: function( event ) {
			if ( !this.spinning ) {
				return;
			}

			clearTimeout( this.timer );
			clearTimeout( this.mousewheelTimer );
			this.counter = 0;
			this.spinning = false;
			this._trigger( "stop", event );
		},

		_setOption: function( key, value ) {
			if ( key === "culture" || key === "numberFormat" ) {
				var prevValue = this._parse( this.element.val() );
				this.options[ key ] = value;
				this.element.val( this._format( prevValue ) );
				return;
			}

			if ( key === "max" || key === "min" || key === "step" ) {
				if ( typeof value === "string" ) {
					value = this._parse( value );
				}
			}
			if ( key === "icons" ) {
				this.buttons.first().find( ".ui-icon" )
					.removeClass( this.options.icons.up )
					.addClass( value.up );
				this.buttons.last().find( ".ui-icon" )
					.removeClass( this.options.icons.down )
					.addClass( value.down );
			}

			this._super( key, value );

			if ( key === "disabled" ) {
				if ( value ) {
					this.element.prop( "disabled", true );
					this.buttons.button( "disable" );
				} else {
					this.element.prop( "disabled", false );
					this.buttons.button( "enable" );
				}
			}
		},

		_setOptions: modifier(function( options ) {
			this._super( options );
			this._value( this.element.val() );
		}),

		_parse: function( val ) {
			if ( typeof val === "string" && val !== "" ) {
				val = window.Globalize && this.options.numberFormat ?
					Globalize.parseFloat( val, 10, this.options.culture ) : +val;
			}
			return val === "" || isNaN( val ) ? null : val;
		},

		_format: function( value ) {
			if ( value === "" ) {
				return "";
			}
			return window.Globalize && this.options.numberFormat ?
				Globalize.format( value, this.options.numberFormat, this.options.culture ) :
				value;
		},

		_refresh: function() {
			this.element.attr({
				"aria-valuemin": this.options.min,
				"aria-valuemax": this.options.max,
				// TODO: what should we do with values that can't be parsed?
				"aria-valuenow": this._parse( this.element.val() )
			});
		},

		// update the value without triggering change
		_value: function( value, allowAny ) {
			var parsed;
			if ( value !== "" ) {
				parsed = this._parse( value );
				if ( parsed !== null ) {
					if ( !allowAny ) {
						parsed = this._adjustValue( parsed );
					}
					value = this._format( parsed );
				}
			}
			this.element.val( value );
			this._refresh();
		},

		_destroy: function() {
			this.element
				.removeClass( "ui-spinner-input" )
				.prop( "disabled", false )
				.removeAttr( "autocomplete" )
				.removeAttr( "role" )
				.removeAttr( "aria-valuemin" )
				.removeAttr( "aria-valuemax" )
				.removeAttr( "aria-valuenow" );
			this.uiSpinner.replaceWith( this.element );
		},

		stepUp: modifier(function( steps ) {
			this._stepUp( steps );
		}),
		_stepUp: function( steps ) {
			if ( this._start() ) {
				this._spin( (steps || 1) * this.options.step );
				this._stop();
			}
		},

		stepDown: modifier(function( steps ) {
			this._stepDown( steps );
		}),
		_stepDown: function( steps ) {
			if ( this._start() ) {
				this._spin( (steps || 1) * -this.options.step );
				this._stop();
			}
		},

		pageUp: modifier(function( pages ) {
			this._stepUp( (pages || 1) * this.options.page );
		}),

		pageDown: modifier(function( pages ) {
			this._stepDown( (pages || 1) * this.options.page );
		}),

		value: function( newVal ) {
			if ( !arguments.length ) {
				return this._parse( this.element.val() );
			}
			modifier( this._value ).call( this, newVal );
		},

		widget: function() {
			return this.uiSpinner;
		}
	});

	}( jQuery ) );
	;
	/*!
	 * jQuery UI Progressbar 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/progressbar/
	 *
	 * Depends:
	 *   jquery.ui.core.js
	 *   jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	$.widget( "ui.progressbar", {
		version: "1.10.4",
		options: {
			max: 100,
			value: 0,

			change: null,
			complete: null
		},

		min: 0,

		_create: function() {
			// Constrain initial value
			this.oldValue = this.options.value = this._constrainedValue();

			this.element
				.addClass( "ui-progressbar ui-widget ui-widget-content ui-corner-all" )
				.attr({
					// Only set static values, aria-valuenow and aria-valuemax are
					// set inside _refreshValue()
					role: "progressbar",
					"aria-valuemin": this.min
				});

			this.valueDiv = $( "<div class='ui-progressbar-value ui-widget-header ui-corner-left'></div>" )
				.appendTo( this.element );

			this._refreshValue();
		},

		_destroy: function() {
			this.element
				.removeClass( "ui-progressbar ui-widget ui-widget-content ui-corner-all" )
				.removeAttr( "role" )
				.removeAttr( "aria-valuemin" )
				.removeAttr( "aria-valuemax" )
				.removeAttr( "aria-valuenow" );

			this.valueDiv.remove();
		},

		value: function( newValue ) {
			if ( newValue === undefined ) {
				return this.options.value;
			}

			this.options.value = this._constrainedValue( newValue );
			this._refreshValue();
		},

		_constrainedValue: function( newValue ) {
			if ( newValue === undefined ) {
				newValue = this.options.value;
			}

			this.indeterminate = newValue === false;

			// sanitize value
			if ( typeof newValue !== "number" ) {
				newValue = 0;
			}

			return this.indeterminate ? false :
				Math.min( this.options.max, Math.max( this.min, newValue ) );
		},

		_setOptions: function( options ) {
			// Ensure "value" option is set after other values (like max)
			var value = options.value;
			delete options.value;

			this._super( options );

			this.options.value = this._constrainedValue( value );
			this._refreshValue();
		},

		_setOption: function( key, value ) {
			if ( key === "max" ) {
				// Don't allow a max less than min
				value = Math.max( this.min, value );
			}

			this._super( key, value );
		},

		_percentage: function() {
			return this.indeterminate ? 100 : 100 * ( this.options.value - this.min ) / ( this.options.max - this.min );
		},

		_refreshValue: function() {
			var value = this.options.value,
				percentage = this._percentage();

			this.valueDiv
				.toggle( this.indeterminate || value > this.min )
				.toggleClass( "ui-corner-right", value === this.options.max )
				.width( percentage.toFixed(0) + "%" );

			this.element.toggleClass( "ui-progressbar-indeterminate", this.indeterminate );

			if ( this.indeterminate ) {
				this.element.removeAttr( "aria-valuenow" );
				if ( !this.overlayDiv ) {
					this.overlayDiv = $( "<div class='ui-progressbar-overlay'></div>" ).appendTo( this.valueDiv );
				}
			} else {
				this.element.attr({
					"aria-valuemax": this.options.max,
					"aria-valuenow": value
				});
				if ( this.overlayDiv ) {
					this.overlayDiv.remove();
					this.overlayDiv = null;
				}
			}

			if ( this.oldValue !== value ) {
				this.oldValue = value;
				this._trigger( "change" );
			}
			if ( value === this.options.max ) {
				this._trigger( "complete" );
			}
		}
	});

	})( jQuery );
	;
	/*!
	 * jQuery UI Tabs 1.10.4
	 * http://jqueryui.com
	 *
	 * Copyright 2014 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * http://api.jqueryui.com/tabs/
	 *
	 * Depends:
	 *	jquery.ui.core.js
	 *	jquery.ui.widget.js
	 */
	(function( $, undefined ) {

	var tabId = 0,
		rhash = /#.*$/;

	function getNextTabId() {
		return ++tabId;
	}

	function isLocal( anchor ) {
		// support: IE7
		// IE7 doesn't normalize the href property when set via script (#9317)
		anchor = anchor.cloneNode( false );

		return anchor.hash.length > 1 &&
			decodeURIComponent( anchor.href.replace( rhash, "" ) ) ===
				decodeURIComponent( location.href.replace( rhash, "" ) );
	}

	$.widget( "ui.tabs", {
		version: "1.10.4",
		delay: 300,
		options: {
			active: null,
			collapsible: false,
			event: "click",
			heightStyle: "content",
			hide: null,
			show: null,

			// callbacks
			activate: null,
			beforeActivate: null,
			beforeLoad: null,
			load: null
		},

		_create: function() {
			var that = this,
				options = this.options;

			this.running = false;

			this.element
				.addClass( "ui-tabs ui-widget ui-widget-content ui-corner-all" )
				.toggleClass( "ui-tabs-collapsible", options.collapsible )
				// Prevent users from focusing disabled tabs via click
				.delegate( ".ui-tabs-nav > li", "mousedown" + this.eventNamespace, function( event ) {
					if ( $( this ).is( ".ui-state-disabled" ) ) {
						event.preventDefault();
					}
				})
				// support: IE <9
				// Preventing the default action in mousedown doesn't prevent IE
				// from focusing the element, so if the anchor gets focused, blur.
				// We don't have to worry about focusing the previously focused
				// element since clicking on a non-focusable element should focus
				// the body anyway.
				.delegate( ".ui-tabs-anchor", "focus" + this.eventNamespace, function() {
					if ( $( this ).closest( "li" ).is( ".ui-state-disabled" ) ) {
						this.blur();
					}
				});

			this._processTabs();
			options.active = this._initialActive();

			// Take disabling tabs via class attribute from HTML
			// into account and update option properly.
			if ( $.isArray( options.disabled ) ) {
				options.disabled = $.unique( options.disabled.concat(
					$.map( this.tabs.filter( ".ui-state-disabled" ), function( li ) {
						return that.tabs.index( li );
					})
				) ).sort();
			}

			// check for length avoids error when initializing empty list
			if ( this.options.active !== false && this.anchors.length ) {
				this.active = this._findActive( options.active );
			} else {
				this.active = $();
			}

			this._refresh();

			if ( this.active.length ) {
				this.load( options.active );
			}
		},

		_initialActive: function() {
			var active = this.options.active,
				collapsible = this.options.collapsible,
				locationHash = location.hash.substring( 1 );

			if ( active === null ) {
				// check the fragment identifier in the URL
				if ( locationHash ) {
					this.tabs.each(function( i, tab ) {
						if ( $( tab ).attr( "aria-controls" ) === locationHash ) {
							active = i;
							return false;
						}
					});
				}

				// check for a tab marked active via a class
				if ( active === null ) {
					active = this.tabs.index( this.tabs.filter( ".ui-tabs-active" ) );
				}

				// no active tab, set to false
				if ( active === null || active === -1 ) {
					active = this.tabs.length ? 0 : false;
				}
			}

			// handle numbers: negative, out of range
			if ( active !== false ) {
				active = this.tabs.index( this.tabs.eq( active ) );
				if ( active === -1 ) {
					active = collapsible ? false : 0;
				}
			}

			// don't allow collapsible: false and active: false
			if ( !collapsible && active === false && this.anchors.length ) {
				active = 0;
			}

			return active;
		},

		_getCreateEventData: function() {
			return {
				tab: this.active,
				panel: !this.active.length ? $() : this._getPanelForTab( this.active )
			};
		},

		_tabKeydown: function( event ) {
			var focusedTab = $( this.document[0].activeElement ).closest( "li" ),
				selectedIndex = this.tabs.index( focusedTab ),
				goingForward = true;

			if ( this._handlePageNav( event ) ) {
				return;
			}

			switch ( event.keyCode ) {
				case $.ui.keyCode.RIGHT:
				case $.ui.keyCode.DOWN:
					selectedIndex++;
					break;
				case $.ui.keyCode.UP:
				case $.ui.keyCode.LEFT:
					goingForward = false;
					selectedIndex--;
					break;
				case $.ui.keyCode.END:
					selectedIndex = this.anchors.length - 1;
					break;
				case $.ui.keyCode.HOME:
					selectedIndex = 0;
					break;
				case $.ui.keyCode.SPACE:
					// Activate only, no collapsing
					event.preventDefault();
					clearTimeout( this.activating );
					this._activate( selectedIndex );
					return;
				case $.ui.keyCode.ENTER:
					// Toggle (cancel delayed activation, allow collapsing)
					event.preventDefault();
					clearTimeout( this.activating );
					// Determine if we should collapse or activate
					this._activate( selectedIndex === this.options.active ? false : selectedIndex );
					return;
				default:
					return;
			}

			// Focus the appropriate tab, based on which key was pressed
			event.preventDefault();
			clearTimeout( this.activating );
			selectedIndex = this._focusNextTab( selectedIndex, goingForward );

			// Navigating with control key will prevent automatic activation
			if ( !event.ctrlKey ) {
				// Update aria-selected immediately so that AT think the tab is already selected.
				// Otherwise AT may confuse the user by stating that they need to activate the tab,
				// but the tab will already be activated by the time the announcement finishes.
				focusedTab.attr( "aria-selected", "false" );
				this.tabs.eq( selectedIndex ).attr( "aria-selected", "true" );

				this.activating = this._delay(function() {
					this.option( "active", selectedIndex );
				}, this.delay );
			}
		},

		_panelKeydown: function( event ) {
			if ( this._handlePageNav( event ) ) {
				return;
			}

			// Ctrl+up moves focus to the current tab
			if ( event.ctrlKey && event.keyCode === $.ui.keyCode.UP ) {
				event.preventDefault();
				this.active.focus();
			}
		},

		// Alt+page up/down moves focus to the previous/next tab (and activates)
		_handlePageNav: function( event ) {
			if ( event.altKey && event.keyCode === $.ui.keyCode.PAGE_UP ) {
				this._activate( this._focusNextTab( this.options.active - 1, false ) );
				return true;
			}
			if ( event.altKey && event.keyCode === $.ui.keyCode.PAGE_DOWN ) {
				this._activate( this._focusNextTab( this.options.active + 1, true ) );
				return true;
			}
		},

		_findNextTab: function( index, goingForward ) {
			var lastTabIndex = this.tabs.length - 1;

			function constrain() {
				if ( index > lastTabIndex ) {
					index = 0;
				}
				if ( index < 0 ) {
					index = lastTabIndex;
				}
				return index;
			}

			while ( $.inArray( constrain(), this.options.disabled ) !== -1 ) {
				index = goingForward ? index + 1 : index - 1;
			}

			return index;
		},

		_focusNextTab: function( index, goingForward ) {
			index = this._findNextTab( index, goingForward );
			this.tabs.eq( index ).focus();
			return index;
		},

		_setOption: function( key, value ) {
			if ( key === "active" ) {
				// _activate() will handle invalid values and update this.options
				this._activate( value );
				return;
			}

			if ( key === "disabled" ) {
				// don't use the widget factory's disabled handling
				this._setupDisabled( value );
				return;
			}

			this._super( key, value);

			if ( key === "collapsible" ) {
				this.element.toggleClass( "ui-tabs-collapsible", value );
				// Setting collapsible: false while collapsed; open first panel
				if ( !value && this.options.active === false ) {
					this._activate( 0 );
				}
			}

			if ( key === "event" ) {
				this._setupEvents( value );
			}

			if ( key === "heightStyle" ) {
				this._setupHeightStyle( value );
			}
		},

		_tabId: function( tab ) {
			return tab.attr( "aria-controls" ) || "ui-tabs-" + getNextTabId();
		},

		_sanitizeSelector: function( hash ) {
			return hash ? hash.replace( /[!"$%&'()*+,.\/:;<=>?@\[\]\^`{|}~]/g, "\\$&" ) : "";
		},

		refresh: function() {
			var options = this.options,
				lis = this.tablist.children( ":has(a[href])" );

			// get disabled tabs from class attribute from HTML
			// this will get converted to a boolean if needed in _refresh()
			options.disabled = $.map( lis.filter( ".ui-state-disabled" ), function( tab ) {
				return lis.index( tab );
			});

			this._processTabs();

			// was collapsed or no tabs
			if ( options.active === false || !this.anchors.length ) {
				options.active = false;
				this.active = $();
			// was active, but active tab is gone
			} else if ( this.active.length && !$.contains( this.tablist[ 0 ], this.active[ 0 ] ) ) {
				// all remaining tabs are disabled
				if ( this.tabs.length === options.disabled.length ) {
					options.active = false;
					this.active = $();
				// activate previous tab
				} else {
					this._activate( this._findNextTab( Math.max( 0, options.active - 1 ), false ) );
				}
			// was active, active tab still exists
			} else {
				// make sure active index is correct
				options.active = this.tabs.index( this.active );
			}

			this._refresh();
		},

		_refresh: function() {
			this._setupDisabled( this.options.disabled );
			this._setupEvents( this.options.event );
			this._setupHeightStyle( this.options.heightStyle );

			this.tabs.not( this.active ).attr({
				"aria-selected": "false",
				tabIndex: -1
			});
			this.panels.not( this._getPanelForTab( this.active ) )
				.hide()
				.attr({
					"aria-expanded": "false",
					"aria-hidden": "true"
				});

			// Make sure one tab is in the tab order
			if ( !this.active.length ) {
				this.tabs.eq( 0 ).attr( "tabIndex", 0 );
			} else {
				this.active
					.addClass( "ui-tabs-active ui-state-active" )
					.attr({
						"aria-selected": "true",
						tabIndex: 0
					});
				this._getPanelForTab( this.active )
					.show()
					.attr({
						"aria-expanded": "true",
						"aria-hidden": "false"
					});
			}
		},

		_processTabs: function() {
			var that = this;

			this.tablist = this._getList()
				.addClass( "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" )
				.attr( "role", "tablist" );

			this.tabs = this.tablist.find( "> li:has(a[href])" )
				.addClass( "ui-state-default ui-corner-top" )
				.attr({
					role: "tab",
					tabIndex: -1
				});

			this.anchors = this.tabs.map(function() {
					return $( "a", this )[ 0 ];
				})
				.addClass( "ui-tabs-anchor" )
				.attr({
					role: "presentation",
					tabIndex: -1
				});

			this.panels = $();

			this.anchors.each(function( i, anchor ) {
				var selector, panel, panelId,
					anchorId = $( anchor ).uniqueId().attr( "id" ),
					tab = $( anchor ).closest( "li" ),
					originalAriaControls = tab.attr( "aria-controls" );

				// inline tab
				if ( isLocal( anchor ) ) {
					selector = anchor.hash;
					panel = that.element.find( that._sanitizeSelector( selector ) );
				// remote tab
				} else {
					panelId = that._tabId( tab );
					selector = "#" + panelId;
					panel = that.element.find( selector );
					if ( !panel.length ) {
						panel = that._createPanel( panelId );
						panel.insertAfter( that.panels[ i - 1 ] || that.tablist );
					}
					panel.attr( "aria-live", "polite" );
				}

				if ( panel.length) {
					that.panels = that.panels.add( panel );
				}
				if ( originalAriaControls ) {
					tab.data( "ui-tabs-aria-controls", originalAriaControls );
				}
				tab.attr({
					"aria-controls": selector.substring( 1 ),
					"aria-labelledby": anchorId
				});
				panel.attr( "aria-labelledby", anchorId );
			});

			this.panels
				.addClass( "ui-tabs-panel ui-widget-content ui-corner-bottom" )
				.attr( "role", "tabpanel" );
		},

		// allow overriding how to find the list for rare usage scenarios (#7715)
		_getList: function() {
			return this.tablist || this.element.find( "ol,ul" ).eq( 0 );
		},

		_createPanel: function( id ) {
			return $( "<div>" )
				.attr( "id", id )
				.addClass( "ui-tabs-panel ui-widget-content ui-corner-bottom" )
				.data( "ui-tabs-destroy", true );
		},

		_setupDisabled: function( disabled ) {
			if ( $.isArray( disabled ) ) {
				if ( !disabled.length ) {
					disabled = false;
				} else if ( disabled.length === this.anchors.length ) {
					disabled = true;
				}
			}

			// disable tabs
			for ( var i = 0, li; ( li = this.tabs[ i ] ); i++ ) {
				if ( disabled === true || $.inArray( i, disabled ) !== -1 ) {
					$( li )
						.addClass( "ui-state-disabled" )
						.attr( "aria-disabled", "true" );
				} else {
					$( li )
						.removeClass( "ui-state-disabled" )
						.removeAttr( "aria-disabled" );
				}
			}

			this.options.disabled = disabled;
		},

		_setupEvents: function( event ) {
			var events = {
				click: function( event ) {
					event.preventDefault();
				}
			};
			if ( event ) {
				$.each( event.split(" "), function( index, eventName ) {
					events[ eventName ] = "_eventHandler";
				});
			}

			this._off( this.anchors.add( this.tabs ).add( this.panels ) );
			this._on( this.anchors, events );
			this._on( this.tabs, { keydown: "_tabKeydown" } );
			this._on( this.panels, { keydown: "_panelKeydown" } );

			this._focusable( this.tabs );
			this._hoverable( this.tabs );
		},

		_setupHeightStyle: function( heightStyle ) {
			var maxHeight,
				parent = this.element.parent();

			if ( heightStyle === "fill" ) {
				maxHeight = parent.height();
				maxHeight -= this.element.outerHeight() - this.element.height();

				this.element.siblings( ":visible" ).each(function() {
					var elem = $( this ),
						position = elem.css( "position" );

					if ( position === "absolute" || position === "fixed" ) {
						return;
					}
					maxHeight -= elem.outerHeight( true );
				});

				this.element.children().not( this.panels ).each(function() {
					maxHeight -= $( this ).outerHeight( true );
				});

				this.panels.each(function() {
					$( this ).height( Math.max( 0, maxHeight -
						$( this ).innerHeight() + $( this ).height() ) );
				})
				.css( "overflow", "auto" );
			} else if ( heightStyle === "auto" ) {
				maxHeight = 0;
				this.panels.each(function() {
					maxHeight = Math.max( maxHeight, $( this ).height( "" ).height() );
				}).height( maxHeight );
			}
		},

		_eventHandler: function( event ) {
			var options = this.options,
				active = this.active,
				anchor = $( event.currentTarget ),
				tab = anchor.closest( "li" ),
				clickedIsActive = tab[ 0 ] === active[ 0 ],
				collapsing = clickedIsActive && options.collapsible,
				toShow = collapsing ? $() : this._getPanelForTab( tab ),
				toHide = !active.length ? $() : this._getPanelForTab( active ),
				eventData = {
					oldTab: active,
					oldPanel: toHide,
					newTab: collapsing ? $() : tab,
					newPanel: toShow
				};

			event.preventDefault();

			if ( tab.hasClass( "ui-state-disabled" ) ||
					// tab is already loading
					tab.hasClass( "ui-tabs-loading" ) ||
					// can't switch durning an animation
					this.running ||
					// click on active header, but not collapsible
					( clickedIsActive && !options.collapsible ) ||
					// allow canceling activation
					( this._trigger( "beforeActivate", event, eventData ) === false ) ) {
				return;
			}

			options.active = collapsing ? false : this.tabs.index( tab );

			this.active = clickedIsActive ? $() : tab;
			if ( this.xhr ) {
				this.xhr.abort();
			}

			if ( !toHide.length && !toShow.length ) {
				$.error( "jQuery UI Tabs: Mismatching fragment identifier." );
			}

			if ( toShow.length ) {
				this.load( this.tabs.index( tab ), event );
			}
			this._toggle( event, eventData );
		},

		// handles show/hide for selecting tabs
		_toggle: function( event, eventData ) {
			var that = this,
				toShow = eventData.newPanel,
				toHide = eventData.oldPanel;

			this.running = true;

			function complete() {
				that.running = false;
				that._trigger( "activate", event, eventData );
			}

			function show() {
				eventData.newTab.closest( "li" ).addClass( "ui-tabs-active ui-state-active" );

				if ( toShow.length && that.options.show ) {
					that._show( toShow, that.options.show, complete );
				} else {
					toShow.show();
					complete();
				}
			}

			// start out by hiding, then showing, then completing
			if ( toHide.length && this.options.hide ) {
				this._hide( toHide, this.options.hide, function() {
					eventData.oldTab.closest( "li" ).removeClass( "ui-tabs-active ui-state-active" );
					show();
				});
			} else {
				eventData.oldTab.closest( "li" ).removeClass( "ui-tabs-active ui-state-active" );
				toHide.hide();
				show();
			}

			toHide.attr({
				"aria-expanded": "false",
				"aria-hidden": "true"
			});
			eventData.oldTab.attr( "aria-selected", "false" );
			// If we're switching tabs, remove the old tab from the tab order.
			// If we're opening from collapsed state, remove the previous tab from the tab order.
			// If we're collapsing, then keep the collapsing tab in the tab order.
			if ( toShow.length && toHide.length ) {
				eventData.oldTab.attr( "tabIndex", -1 );
			} else if ( toShow.length ) {
				this.tabs.filter(function() {
					return $( this ).attr( "tabIndex" ) === 0;
				})
				.attr( "tabIndex", -1 );
			}

			toShow.attr({
				"aria-expanded": "true",
				"aria-hidden": "false"
			});
			eventData.newTab.attr({
				"aria-selected": "true",
				tabIndex: 0
			});
		},

		_activate: function( index ) {
			var anchor,
				active = this._findActive( index );

			// trying to activate the already active panel
			if ( active[ 0 ] === this.active[ 0 ] ) {
				return;
			}

			// trying to collapse, simulate a click on the current active header
			if ( !active.length ) {
				active = this.active;
			}

			anchor = active.find( ".ui-tabs-anchor" )[ 0 ];
			this._eventHandler({
				target: anchor,
				currentTarget: anchor,
				preventDefault: $.noop
			});
		},

		_findActive: function( index ) {
			return index === false ? $() : this.tabs.eq( index );
		},

		_getIndex: function( index ) {
			// meta-function to give users option to provide a href string instead of a numerical index.
			if ( typeof index === "string" ) {
				index = this.anchors.index( this.anchors.filter( "[href$='" + index + "']" ) );
			}

			return index;
		},

		_destroy: function() {
			if ( this.xhr ) {
				this.xhr.abort();
			}

			this.element.removeClass( "ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible" );

			this.tablist
				.removeClass( "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" )
				.removeAttr( "role" );

			this.anchors
				.removeClass( "ui-tabs-anchor" )
				.removeAttr( "role" )
				.removeAttr( "tabIndex" )
				.removeUniqueId();

			this.tabs.add( this.panels ).each(function() {
				if ( $.data( this, "ui-tabs-destroy" ) ) {
					$( this ).remove();
				} else {
					$( this )
						.removeClass( "ui-state-default ui-state-active ui-state-disabled " +
							"ui-corner-top ui-corner-bottom ui-widget-content ui-tabs-active ui-tabs-panel" )
						.removeAttr( "tabIndex" )
						.removeAttr( "aria-live" )
						.removeAttr( "aria-busy" )
						.removeAttr( "aria-selected" )
						.removeAttr( "aria-labelledby" )
						.removeAttr( "aria-hidden" )
						.removeAttr( "aria-expanded" )
						.removeAttr( "role" );
				}
			});

			this.tabs.each(function() {
				var li = $( this ),
					prev = li.data( "ui-tabs-aria-controls" );
				if ( prev ) {
					li
						.attr( "aria-controls", prev )
						.removeData( "ui-tabs-aria-controls" );
				} else {
					li.removeAttr( "aria-controls" );
				}
			});

			this.panels.show();

			if ( this.options.heightStyle !== "content" ) {
				this.panels.css( "height", "" );
			}
		},

		enable: function( index ) {
			var disabled = this.options.disabled;
			if ( disabled === false ) {
				return;
			}

			if ( index === undefined ) {
				disabled = false;
			} else {
				index = this._getIndex( index );
				if ( $.isArray( disabled ) ) {
					disabled = $.map( disabled, function( num ) {
						return num !== index ? num : null;
					});
				} else {
					disabled = $.map( this.tabs, function( li, num ) {
						return num !== index ? num : null;
					});
				}
			}
			this._setupDisabled( disabled );
		},

		disable: function( index ) {
			var disabled = this.options.disabled;
			if ( disabled === true ) {
				return;
			}

			if ( index === undefined ) {
				disabled = true;
			} else {
				index = this._getIndex( index );
				if ( $.inArray( index, disabled ) !== -1 ) {
					return;
				}
				if ( $.isArray( disabled ) ) {
					disabled = $.merge( [ index ], disabled ).sort();
				} else {
					disabled = [ index ];
				}
			}
			this._setupDisabled( disabled );
		},

		load: function( index, event ) {
			index = this._getIndex( index );
			var that = this,
				tab = this.tabs.eq( index ),
				anchor = tab.find( ".ui-tabs-anchor" ),
				panel = this._getPanelForTab( tab ),
				eventData = {
					tab: tab,
					panel: panel
				};

			// not remote
			if ( isLocal( anchor[ 0 ] ) ) {
				return;
			}

			this.xhr = $.ajax( this._ajaxSettings( anchor, event, eventData ) );

			// support: jQuery <1.8
			// jQuery <1.8 returns false if the request is canceled in beforeSend,
			// but as of 1.8, $.ajax() always returns a jqXHR object.
			if ( this.xhr && this.xhr.statusText !== "canceled" ) {
				tab.addClass( "ui-tabs-loading" );
				panel.attr( "aria-busy", "true" );

				this.xhr
					.success(function( response ) {
						// support: jQuery <1.8
						// http://bugs.jquery.com/ticket/11778
						setTimeout(function() {
							panel.html( response );
							that._trigger( "load", event, eventData );
						}, 1 );
					})
					.complete(function( jqXHR, status ) {
						// support: jQuery <1.8
						// http://bugs.jquery.com/ticket/11778
						setTimeout(function() {
							if ( status === "abort" ) {
								that.panels.stop( false, true );
							}

							tab.removeClass( "ui-tabs-loading" );
							panel.removeAttr( "aria-busy" );

							if ( jqXHR === that.xhr ) {
								delete that.xhr;
							}
						}, 1 );
					});
			}
		},

		_ajaxSettings: function( anchor, event, eventData ) {
			var that = this;
			return {
				url: anchor.attr( "href" ),
				beforeSend: function( jqXHR, settings ) {
					return that._trigger( "beforeLoad", event,
						$.extend( { jqXHR : jqXHR, ajaxSettings: settings }, eventData ) );
				}
			};
		},

		_getPanelForTab: function( tab ) {
			var id = $( tab ).attr( "aria-controls" );
			return this.element.find( this._sanitizeSelector( "#" + id ) );
		}
	});

	})( jQuery );
	;

	/*
	 * Class that provides the top navbar functionality.
	 *
	 * @class MainNavbar
	 */

	(function() {
	  PixelAdmin.MainNavbar = function() {
	    this._scroller = false;
	    this._wheight = null;
	    this.scroll_pos = 0;
	    return this;
	  };


	  /*
	   * Initialize plugin.
	   */

	  PixelAdmin.MainNavbar.prototype.init = function() {
	    var is_mobile;
	    this.$navbar = $('#main-navbar');
	    this.$header = this.$navbar.find('.navbar-header');
	    this.$toggle = this.$navbar.find('.navbar-toggle:first');
	    this.$collapse = $('#main-navbar-collapse');
	    this.$collapse_div = this.$collapse.find('> div');
	    is_mobile = false;
	    $(window).on('pa.screen.small pa.screen.tablet', (function(_this) {
	      return function() {
	        if (_this.$navbar.css('position') === 'fixed') {
	          _this._setupScroller();
	        }
	        return is_mobile = true;
	      };
	    })(this)).on('pa.screen.desktop', (function(_this) {
	      return function() {
	        _this._removeScroller();
	        return is_mobile = false;
	      };
	    })(this));
	    return this.$navbar.on('click', '.nav-icon-btn.dropdown > .dropdown-toggle', function(e) {
	      if (is_mobile) {
	        e.preventDefault();
	        e.stopPropagation();
	        document.location.href = $(this).attr('href');
	        return false;
	      }
	    });
	  };


	  /*
	   * Attach scroller to navbar collapse.
	   */

	  PixelAdmin.MainNavbar.prototype._setupScroller = function() {
	    if (this._scroller) {
	      return;
	    }
	    this._scroller = true;
	    this.$collapse_div.pixelSlimScroll({});
	    this.$navbar.on('shown.bs.collapse.mn_collapse', $.proxy(((function(_this) {
	      return function() {
	        _this._updateCollapseHeight();
	        return _this._watchWindowHeight();
	      };
	    })(this)), this)).on('hidden.bs.collapse.mn_collapse', $.proxy(((function(_this) {
	      return function() {
	        _this._wheight = null;
	        return _this.$collapse_div.pixelSlimScroll({
	          scrollTo: '0px'
	        });
	      };
	    })(this)), this)).on('shown.bs.dropdown.mn_collapse', $.proxy(this._updateCollapseHeight, this)).on('hidden.bs.dropdown.mn_collapse', $.proxy(this._updateCollapseHeight, this));
	    return this._updateCollapseHeight();
	  };


	  /*
	   * Detach scroller from navbar collapse.
	   */

	  PixelAdmin.MainNavbar.prototype._removeScroller = function() {
	    if (!this._scroller) {
	      return;
	    }
	    this._wheight = null;
	    this._scroller = false;
	    this.$collapse_div.pixelSlimScroll({
	      destroy: 'destroy'
	    });
	    this.$navbar.off('shown.bs.collapse.mn_collapse');
	    this.$navbar.off('hidden.bs.collapse.mn_collapse');
	    this.$navbar.off('shown.bs.dropdown.mn_collapse');
	    this.$navbar.off('hidden.bs.dropdown.mn_collapse');
	    return this.$collapse.attr('style', '');
	  };


	  /*
	   * Update navbar collapse height.
	   */

	  PixelAdmin.MainNavbar.prototype._updateCollapseHeight = function() {
	    var h_height, scrollTop, w_height;
	    if (!this._scroller) {
	      return;
	    }
	    w_height = $(window).innerHeight();
	    h_height = this.$header.outerHeight();
	    scrollTop = this.$collapse_div.scrollTop();
	    if ((h_height + this.$collapse_div.css({
	      'max-height': 'none'
	    }).outerHeight()) > w_height) {
	      this.$collapse_div.css({
	        'max-height': w_height - h_height
	      });
	    } else {
	      this.$collapse_div.css({
	        'max-height': 'none'
	      });
	    }
	    return this.$collapse_div.pixelSlimScroll({
	      scrollTo: scrollTop + 'px'
	    });
	  };


	  /*
	   * Detecting a change of the window height.
	   */

	  PixelAdmin.MainNavbar.prototype._watchWindowHeight = function() {
	    var checkWindowInnerHeight;
	    this._wheight = $(window).innerHeight();
	    checkWindowInnerHeight = (function(_this) {
	      return function() {
	        if (_this._wheight === null) {
	          return;
	        }
	        if (_this._wheight !== $(window).innerHeight()) {
	          _this._updateCollapseHeight();
	        }
	        _this._wheight = $(window).innerHeight();
	        return setTimeout(checkWindowInnerHeight, 100);
	      };
	    })(this);
	    return window.setTimeout(checkWindowInnerHeight, 100);
	  };

	  PixelAdmin.MainNavbar.Constructor = PixelAdmin.MainNavbar;

	  PixelAdmin.addInitializer(function() {
	    return PixelAdmin.initPlugin('main_navbar', new PixelAdmin.MainNavbar);
	  });

	}).call(this);
	;

	/*
	 * Class that provides the main menu functionality.
	 *
	 * @class MainMenu
	 */

	(function() {
	  PixelAdmin.MainMenu = function() {
	    this._screen = null;
	    this._last_screen = null;
	    this._animate = false;
	    this._close_timer = null;
	    this._dropdown_li = null;
	    this._dropdown = null;
	    return this;
	  };


	  /*
	   * Initialize plugin.
	   */

	  PixelAdmin.MainMenu.prototype.init = function() {
	    var self, state;
	    this.$menu = $('#main-menu');
	    if (!this.$menu.length) {
	      return;
	    }
	    this.$body = $('body');
	    this.menu = this.$menu[0];
	    this.$ssw_point = $('#small-screen-width-point');
	    this.$tsw_point = $('#tablet-screen-width-point');
	    self = this;
	    if (PixelAdmin.settings.main_menu.store_state) {
	      state = this._getMenuState();
	      document.body.className += ' disable-mm-animation';
	      if (state !== null) {
	        this.$body[state === 'collapsed' ? 'addClass' : 'removeClass']('mmc');
	      }
	      setTimeout((function(_this) {
	        return function() {
	          return elRemoveClass(document.body, 'disable-mm-animation');
	        };
	      })(this), 20);
	    }
	    this.setupAnimation();
	    $(window).on('resize.pa.mm', $.proxy(this.onResize, this));
	    this.onResize();
	    this.$menu.find('.navigation > .mm-dropdown').addClass('mm-dropdown-root');
	    if (PixelAdmin.settings.main_menu.detect_active) {
	      this.detectActiveItem();
	    }
	    if ($.support.transition) {
	      this.$menu.on('transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd', $.proxy(this._onAnimationEnd, this));
	    }
	    $('#main-menu-toggle').on('click', $.proxy(this.toggle, this));
	    $('#main-menu-inner').slimScroll({
	      height: '100%'
	    }).on('slimscrolling', (function(_this) {
	      return function() {
	        return _this.closeCurrentDropdown(true);
	      };
	    })(this));
	    this.$menu.on('click', '.mm-dropdown > a', function() {
	      var li;
	      li = this.parentNode;
	      if (elHasClass(li, 'mm-dropdown-root') && self._collapsed()) {
	        if (elHasClass(li, 'mmc-dropdown-open')) {
	          if (elHasClass(li, 'freeze')) {
	            self.closeCurrentDropdown(true);
	          } else {
	            self.freezeDropdown(li);
	          }
	        } else {
	          self.openDropdown(li, true);
	        }
	      } else {
	        self.toggleSubmenu(li);
	      }
	      return false;
	    });
	    this.$menu.find('.navigation').on('mouseenter.pa.mm-dropdown', '.mm-dropdown-root', function() {
	      self.clearCloseTimer();
	      if (self._dropdown_li === this) {
	        return;
	      }
	      if (self._collapsed() && (!self._dropdown_li || !elHasClass(self._dropdown_li, 'freeze'))) {
	        return self.openDropdown(this);
	      }
	    }).on('mouseleave.pa.mm-dropdown', '.mm-dropdown-root', function() {
	      return self._close_timer = setTimeout(function() {
	        return self.closeCurrentDropdown();
	      }, PixelAdmin.settings.main_menu.dropdown_close_delay);
	    });
	    return this;
	  };

	  PixelAdmin.MainMenu.prototype._collapsed = function() {
	    return (this._screen === 'desktop' && elHasClass(document.body, 'mmc')) || (this._screen !== 'desktop' && !elHasClass(document.body, 'mme'));
	  };

	  PixelAdmin.MainMenu.prototype.onResize = function() {
	    this._screen = getScreenSize(this.$ssw_point, this.$tsw_point);
	    this._animate = PixelAdmin.settings.main_menu.disable_animation_on.indexOf(screen) === -1;
	    if (this._dropdown_li) {
	      this.closeCurrentDropdown(true);
	    }
	    if ((this._screen === 'small' && this._last_screen !== this._screen) || (this._screen === 'tablet' && this._last_screen === 'small')) {
	      document.body.className += ' disable-mm-animation';
	      setTimeout((function(_this) {
	        return function() {
	          return elRemoveClass(document.body, 'disable-mm-animation');
	        };
	      })(this), 20);
	    }
	    return this._last_screen = this._screen;
	  };

	  PixelAdmin.MainMenu.prototype.clearCloseTimer = function() {
	    if (this._close_timer) {
	      clearTimeout(this._close_timer);
	      return this._close_timer = null;
	    }
	  };

	  PixelAdmin.MainMenu.prototype._onAnimationEnd = function(e) {
	    if (this._screen !== 'desktop' || e.target.id !== 'main-menu') {
	      return;
	    }
	    return $(window).trigger('resize');
	  };

	  PixelAdmin.MainMenu.prototype.toggle = function() {
	    var cls, collapse;
	    cls = this._screen === 'small' || this._screen === 'tablet' ? 'mme' : 'mmc';
	    if (elHasClass(document.body, cls)) {
	      elRemoveClass(document.body, cls);
	    } else {
	      document.body.className += ' ' + cls;
	    }
	    if (cls === 'mmc') {
	      if (PixelAdmin.settings.main_menu.store_state) {
	        this._storeMenuState(elHasClass(document.body, 'mmc'));
	      }
	      if (!$.support.transition) {
	        return $(window).trigger('resize');
	      }
	    } else {
	      collapse = document.getElementById('');
	      $('#main-navbar-collapse').stop().removeClass('in collapsing').addClass('collapse')[0].style.height = '0px';
	      return $('#main-navbar .navbar-toggle').addClass('collapsed');
	    }
	  };

	  PixelAdmin.MainMenu.prototype.toggleSubmenu = function(li) {
	    this[elHasClass(li, 'open') ? 'collapseSubmenu' : 'expandSubmenu'](li);
	    return false;
	  };

	  PixelAdmin.MainMenu.prototype.collapseSubmenu = function(li) {
	    var $li, $ul;
	    $li = $(li);
	    $ul = $li.find('> ul');
	    if (this._animate) {
	      $ul.animate({
	        height: 0
	      }, PixelAdmin.settings.main_menu.animation_speed, (function(_this) {
	        return function() {
	          elRemoveClass(li, 'open');
	          $ul.attr('style', '');
	          return $li.find('.mm-dropdown.open').removeClass('open').find('> ul').attr('style', '');
	        };
	      })(this));
	    } else {
	      elRemoveClass(li, 'open');
	    }
	    return false;
	  };

	  PixelAdmin.MainMenu.prototype.expandSubmenu = function(li) {
	    var $li, $ul, h, ul;
	    $li = $(li);
	    if (PixelAdmin.settings.main_menu.accordion) {
	      this.collapseAllSubmenus(li);
	    }
	    if (this._animate) {
	      $ul = $li.find('> ul');
	      ul = $ul[0];
	      ul.className += ' get-height';
	      h = $ul.height();
	      elRemoveClass(ul, 'get-height');
	      ul.style.display = 'block';
	      ul.style.height = '0px';
	      li.className += ' open';
	      return $ul.animate({
	        height: h
	      }, PixelAdmin.settings.main_menu.animation_speed, (function(_this) {
	        return function() {
	          return $ul.attr('style', '');
	        };
	      })(this));
	    } else {
	      return li.className += ' open';
	    }
	  };

	  PixelAdmin.MainMenu.prototype.collapseAllSubmenus = function(li) {
	    var self;
	    self = this;
	    return $(li).parent().find('> .mm-dropdown.open').each(function() {
	      return self.collapseSubmenu(this);
	    });
	  };

	  PixelAdmin.MainMenu.prototype.openDropdown = function(li, freeze) {
	    var $li, $title, $ul, $wrapper, max_height, min_height, title_h, top, ul, w_height, wrapper;
	    if (freeze == null) {
	      freeze = false;
	    }
	    if (this._dropdown_li) {
	      this.closeCurrentDropdown(freeze);
	    }
	    $li = $(li);
	    $ul = $li.find('> ul');
	    ul = $ul[0];
	    this._dropdown_li = li;
	    this._dropdown = ul;
	    $title = $ul.find('> .mmc-title');
	    if (!$title.length) {
	      $title = $('<div class="mmc-title"></div>').text($li.find('> a > .mm-text').text());
	      ul.insertBefore($title[0], ul.firstChild);
	    }
	    li.className += ' mmc-dropdown-open';
	    ul.className += ' mmc-dropdown-open-ul';
	    top = $li.position().top;
	    if (elHasClass(document.body, 'main-menu-fixed')) {
	      $wrapper = $ul.find('.mmc-wrapper');
	      if (!$wrapper.length) {
	        wrapper = document.createElement('div');
	        wrapper.className = 'mmc-wrapper';
	        wrapper.style.overflow = 'hidden';
	        wrapper.style.position = 'relative';
	        $wrapper = $(wrapper);
	        $wrapper.append($ul.find('> li'));
	        ul.appendChild(wrapper);
	      }
	      w_height = $(window).innerHeight();
	      title_h = $title.outerHeight();
	      min_height = title_h + $ul.find('.mmc-wrapper > li').first().outerHeight() * 3;
	      if ((top + min_height) > w_height) {
	        max_height = top - $('#main-navbar').outerHeight();
	        ul.className += ' top';
	        ul.style.bottom = (w_height - top - title_h) + 'px';
	      } else {
	        max_height = w_height - top - title_h;
	        ul.style.top = top + 'px';
	      }
	      if (elHasClass(ul, 'top')) {
	        ul.appendChild($title[0]);
	      } else {
	        ul.insertBefore($title[0], ul.firstChild);
	      }
	      li.className += ' slimscroll-attached';
	      $wrapper[0].style.maxHeight = (max_height - 10) + 'px';
	      $wrapper.pixelSlimScroll({});
	    } else {
	      ul.style.top = top + 'px';
	    }
	    if (freeze) {
	      this.freezeDropdown(li);
	    }
	    if (!freeze) {
	      $ul.on('mouseenter', (function(_this) {
	        return function() {
	          return _this.clearCloseTimer();
	        };
	      })(this)).on('mouseleave', (function(_this) {
	        return function() {
	          return _this._close_timer = setTimeout(function() {
	            return _this.closeCurrentDropdown();
	          }, PixelAdmin.settings.main_menu.dropdown_close_delay);
	        };
	      })(this));
	      this;
	    }
	    return this.menu.appendChild(ul);
	  };

	  PixelAdmin.MainMenu.prototype.closeCurrentDropdown = function(force) {
	    var $dropdown, $wrapper;
	    if (force == null) {
	      force = false;
	    }
	    if (!this._dropdown_li || (elHasClass(this._dropdown_li, 'freeze') && !force)) {
	      return;
	    }
	    this.clearCloseTimer();
	    $dropdown = $(this._dropdown);
	    if (elHasClass(this._dropdown_li, 'slimscroll-attached')) {
	      elRemoveClass(this._dropdown_li, 'slimscroll-attached');
	      $wrapper = $dropdown.find('.mmc-wrapper');
	      $wrapper.pixelSlimScroll({
	        destroy: 'destroy'
	      }).find('> *').appendTo($dropdown);
	      $wrapper.remove();
	    }
	    this._dropdown_li.appendChild(this._dropdown);
	    elRemoveClass(this._dropdown, 'mmc-dropdown-open-ul');
	    elRemoveClass(this._dropdown, 'top');
	    elRemoveClass(this._dropdown_li, 'mmc-dropdown-open');
	    elRemoveClass(this._dropdown_li, 'freeze');
	    $(this._dropdown_li).attr('style', '');
	    $dropdown.attr('style', '').off('mouseenter').off('mouseleave');
	    this._dropdown = null;
	    return this._dropdown_li = null;
	  };

	  PixelAdmin.MainMenu.prototype.freezeDropdown = function(li) {
	    return li.className += ' freeze';
	  };

	  PixelAdmin.MainMenu.prototype.setupAnimation = function() {
	    var $mm, $mm_nav, d_body, dsbl_animation_on;
	    d_body = document.body;
	    dsbl_animation_on = PixelAdmin.settings.main_menu.disable_animation_on;
	    d_body.className += ' dont-animate-mm-content';
	    $mm = $('#main-menu');
	    $mm_nav = $mm.find('.navigation');
	    $mm_nav.find('> .mm-dropdown > ul').addClass('mmc-dropdown-delay animated');
	    $mm_nav.find('> li > a > .mm-text').addClass('mmc-dropdown-delay animated fadeIn');
	    $mm.find('.menu-content').addClass('animated fadeIn');
	    if (elHasClass(d_body, 'main-menu-right') || (elHasClass(d_body, 'right-to-left') && !elHasClass(d_body, 'main-menu-right'))) {
	      $mm_nav.find('> .mm-dropdown > ul').addClass('fadeInRight');
	    } else {
	      $mm_nav.find('> .mm-dropdown > ul').addClass('fadeInLeft');
	    }
	    d_body.className += dsbl_animation_on.indexOf('small') === -1 ? ' animate-mm-sm' : ' dont-animate-mm-content-sm';
	    d_body.className += dsbl_animation_on.indexOf('tablet') === -1 ? ' animate-mm-md' : ' dont-animate-mm-content-md';
	    d_body.className += dsbl_animation_on.indexOf('desktop') === -1 ? ' animate-mm-lg' : ' dont-animate-mm-content-lg';
	    return window.setTimeout(function() {
	      return elRemoveClass(d_body, 'dont-animate-mm-content');
	    }, 500);
	  };

	  PixelAdmin.MainMenu.prototype.detectActiveItem = function() {
	    var a, bubble, links, nav, predicate, url, _i, _len, _results;
	    url = (document.location + '').replace(/\#.*?$/, '');
	    predicate = PixelAdmin.settings.main_menu.detect_active_predicate;
	    nav = $('#main-menu .navigation');
	    nav.find('li').removeClass('open active');
	    links = nav[0].getElementsByTagName('a');
	    bubble = (function(_this) {
	      return function(li) {
	        li.className += ' active';
	        if (!elHasClass(li.parentNode, 'navigation')) {
	          li = li.parentNode.parentNode;
	          li.className += ' open';
	          return bubble(li);
	        }
	      };
	    })(this);
	    _results = [];
	    for (_i = 0, _len = links.length; _i < _len; _i++) {
	      a = links[_i];
	      if (a.href.indexOf('#') === -1 && predicate(a.href, url)) {
	        bubble(a.parentNode);
	        break;
	      } else {
	        _results.push(void 0);
	      }
	    }
	    return _results;
	  };


	  /*
	   * Load menu state.
	   */

	  PixelAdmin.MainMenu.prototype._getMenuState = function() {
	    return PixelAdmin.getStoredValue(PixelAdmin.settings.main_menu.store_state_key, null);
	  };


	  /*
	   * Store menu state.
	   */

	  PixelAdmin.MainMenu.prototype._storeMenuState = function(is_collapsed) {
	    if (!PixelAdmin.settings.main_menu.store_state) {
	      return;
	    }
	    return PixelAdmin.storeValue(PixelAdmin.settings.main_menu.store_state_key, is_collapsed ? 'collapsed' : 'expanded');
	  };

	  PixelAdmin.MainMenu.Constructor = PixelAdmin.MainMenu;

	  PixelAdmin.addInitializer(function() {
	    return PixelAdmin.initPlugin('main_menu', new PixelAdmin.MainMenu);
	  });

	}).call(this);
	;
	(function() {
	  var ALERTS_CONTAINER_ID, Alert;

	  ALERTS_CONTAINER_ID = 'pa-page-alerts-box';


	  /*
	   * @class Alert
	   */

	  Alert = function() {
	    return this;
	  };

	  Alert.DEFAULTS = {
	    type: 'warning',
	    close_btn: true,
	    classes: false,
	    namespace: 'pa_page_alerts',
	    animate: true,
	    auto_close: false
	  };

	  Alert.TYPES_HASH = {
	    warning: '',
	    danger: 'alert-danger',
	    success: 'alert-success',
	    info: 'alert-info'
	  };


	  /*
	   * Initialize plugin.
	   */

	  Alert.prototype.init = function() {
	    var self;
	    self = this;
	    return $('#main-wrapper').on('click.pa.alerts', '#' + ALERTS_CONTAINER_ID + ' .close', function() {
	      self.close($(this).parents('.alert'));
	      return false;
	    });
	  };


	  /*
	   * Add new alert.
	   *
	   * @param  {String} html
	   * @param  {Object} options
	   */

	  Alert.prototype.add = function(html, options) {
	    var $alert, $alerts, $box, height, padding_bottom, padding_top;
	    options = $.extend({}, Alert.DEFAULTS, options || {});
	    $alert = $('<div class="alert alert-page ' + options.namespace + ' ' + Alert.TYPES_HASH[options.type] + '" />').html(html);
	    if (options.classes) {
	      $alert.addClass(options.classes);
	    }
	    if (options.close_btn) {
	      $alert.prepend($('<button type="button" class="close" />').html('&times;'));
	    }
	    if (options.animate) {
	      $alert.attr('data-animate', 'true');
	    }
	    $box = $('#' + ALERTS_CONTAINER_ID);
	    if (!$box.length) {
	      $box = $('<div id="' + ALERTS_CONTAINER_ID + '" />').prependTo($('#content-wrapper'));
	    }
	    $alerts = $('#' + ALERTS_CONTAINER_ID + ' .' + options.namespace);
	    height = $alert.css({
	      visibility: 'hidden',
	      position: 'absolute',
	      width: '100%'
	    }).appendTo('body').outerHeight();
	    padding_top = $alert.css('padding-top');
	    padding_bottom = $alert.css('padding-bottom');
	    if (options.animate) {
	      $alert.attr('style', '').css({
	        overflow: 'hidden',
	        height: 0,
	        'padding-top': 0,
	        'padding-bottom': 0
	      });
	    }
	    if ($alerts.length) {
	      $alerts.last().after($alert);
	    } else {
	      $box.append($alert);
	    }
	    if (options.animate) {
	      return $alert.animate({
	        'height': height,
	        'padding-top': padding_top,
	        'padding-bottom': padding_bottom
	      }, 500, (function(_this) {
	        return function() {
	          $alert.attr('style', '');
	          if (options.auto_close) {
	            return $.data($alert, 'timer', setTimeout(function() {
	              return _this.close($alert);
	            }, options.auto_close * 1000));
	          }
	        };
	      })(this));
	    } else {
	      return $alert.attr('style', '');
	    }
	  };


	  /*
	   * Close alert.
	   *
	   * @param  {jQuery Object} $alert
	   */

	  Alert.prototype.close = function($alert) {
	    if ($alert.attr('data-animate') === 'true') {
	      return $alert.animate({
	        'height': 0,
	        'padding-top': 0,
	        'padding-bottom': 0
	      }, 500, function() {
	        if ($.data($alert, 'timer')) {
	          clearTimeout($.data($alert, 'timer'));
	        }
	        return $alert.remove();
	      });
	    } else {
	      if ($.data($alert, 'timer')) {
	        clearTimeout($.data($alert, 'timer'));
	      }
	      return $alert.remove();
	    }
	  };


	  /*
	   * Close all alerts with specified namespace.
	   *
	   * @param  {Boolean} animate
	   * @param  {String} namespace
	   */

	  Alert.prototype.clear = function(animate, namespace) {
	    var $alerts, self;
	    if (animate == null) {
	      animate = true;
	    }
	    if (namespace == null) {
	      namespace = 'pa_page_alerts';
	    }
	    $alerts = $('#' + ALERTS_CONTAINER_ID + ' .' + namespace);
	    if ($alerts.length) {
	      self = this;
	      if (animate) {
	        return $alerts.each(function() {
	          return self.close($(this));
	        });
	      } else {
	        return $alerts.remove();
	      }
	    }
	  };


	  /*
	   * Close all alerts.
	   *
	   * @param  {Boolean} animate
	   */

	  Alert.prototype.clearAll = function(animate) {
	    var self;
	    if (animate == null) {
	      animate = true;
	    }
	    if (animate) {
	      self = this;
	      return $('#' + ALERTS_CONTAINER_ID + ' .alert').each(function() {
	        return self.close($(this));
	      });
	    } else {
	      return $('#' + ALERTS_CONTAINER_ID).remove();
	    }
	  };


	  /*
	   * Returns alerts container ID.
	   */

	  Alert.prototype.getContainerId = function() {
	    return ALERTS_CONTAINER_ID;
	  };

	  Alert.Constructor = Alert;

	  PixelAdmin.addInitializer(function() {
	    return PixelAdmin.initPlugin('alerts', new Alert);
	  });

	}).call(this);
	;

	/*
	 * @class Switcher
	 */

	(function() {
	  var Switcher;

	  Switcher = function($el, options) {
	    var box_class;
	    if (options == null) {
	      options = {};
	    }
	    this.options = $.extend({}, Switcher.DEFAULTS, options);
	    this.$checkbox = null;
	    this.$box = null;
	    if ($el.is('input[type="checkbox"]')) {
	      box_class = $el.attr('data-class');
	      this.$checkbox = $el;
	      this.$box = $('<div class="switcher"><div class="switcher-toggler"></div><div class="switcher-inner"><div class="switcher-state-on">' + this.options.on_state_content + '</div><div class="switcher-state-off">' + this.options.off_state_content + '</div></div></div>');
	      if (this.options.theme) {
	        this.$box.addClass('switcher-theme-' + this.options.theme);
	      }
	      if (box_class) {
	        this.$box.addClass(box_class);
	      }
	      this.$box.insertAfter(this.$checkbox).prepend(this.$checkbox);
	    } else {
	      this.$box = $el;
	      this.$checkbox = $('input[type="checkbox"]', this.$box);
	    }
	    if (this.$checkbox.prop('disabled')) {
	      this.$box.addClass('disabled');
	    }
	    if (this.$checkbox.is(':checked')) {
	      this.$box.addClass('checked');
	    }
	    this.$checkbox.on('click', function(e) {
	      return e.stopPropagation();
	    });
	    this.$box.on('touchend click', (function(_this) {
	      return function(e) {
	        e.stopPropagation();
	        e.preventDefault();
	        return _this.toggle();
	      };
	    })(this));
	    return this;
	  };


	  /*
	   * Enable switcher.
	   *
	   */

	  Switcher.prototype.enable = function() {
	    this.$checkbox.prop('disabled', false);
	    return this.$box.removeClass('disabled');
	  };


	  /*
	   * Disable switcher.
	   *
	   */

	  Switcher.prototype.disable = function() {
	    this.$checkbox.prop('disabled', true);
	    return this.$box.addClass('disabled');
	  };


	  /*
	   * Set switcher to true.
	   *
	   */

	  Switcher.prototype.on = function() {
	    if (!this.$checkbox.is(':checked')) {
	      this.$checkbox.click();
	      return this.$box.addClass('checked');
	    }
	  };


	  /*
	   * Set switcher to false.
	   *
	   */

	  Switcher.prototype.off = function() {
	    if (this.$checkbox.is(':checked')) {
	      this.$checkbox.click();
	      return this.$box.removeClass('checked');
	    }
	  };


	  /*
	   * Toggle switcher.
	   *
	   */

	  Switcher.prototype.toggle = function() {
	    if (this.$checkbox.click().is(':checked')) {
	      return this.$box.addClass('checked');
	    } else {
	      return this.$box.removeClass('checked');
	    }
	  };

	  Switcher.DEFAULTS = {
	    theme: null,
	    on_state_content: 'ON',
	    off_state_content: 'OFF'
	  };

	  $.fn.switcher = function(options, attrs) {
	    return $(this).each(function() {
	      var $this, sw;
	      $this = $(this);
	      sw = $.data(this, 'Switcher');
	      if (!sw) {
	        return $.data(this, 'Switcher', new Switcher($this, options));
	      } else if (options === 'enable') {
	        return sw.enable();
	      } else if (options === 'disable') {
	        return sw.disable();
	      } else if (options === 'on') {
	        return sw.on();
	      } else if (options === 'off') {
	        return sw.off();
	      } else if (options === 'toggle') {
	        return sw.toggle();
	      }
	    });
	  };

	  $.fn.switcher.Constructor = Switcher;

	}).call(this);
	;

	/*
	 * @class Limiter
	 */

	(function() {
	  var Limiter;

	  Limiter = function($el, limit, options) {
	    if (options == null) {
	      options = {};
	    }
	    this.limit = limit;
	    this.$el = $el;
	    this.options = $.extend({}, Limiter.DEFAULTS, options || {});
	    this.$label = this.options.label && $(this.options.label).length ? $('.limiter-count', this.options.label) : null;
	    this.textarea = this.$el.is('textarea');
	    if (!this.textarea) {
	      this.$el.attr('maxlength', this.limit);
	    }
	    this.$el.on("keyup focus", $.proxy(this.updateCounter, this));
	    return this.updateCounter();
	  };

	  Limiter.prototype.updateCounter = function() {
	    var chars_count, input_value;
	    input_value = this.textarea ? this.$el[0].value.replace(/\r?\n/g, "\n") : this.$el.val();
	    chars_count = input_value.length;
	    if (chars_count > this.limit) {
	      this.$el.val(input_value.substr(0, this.limit));
	      chars_count = this.limit;
	    }
	    if (this.$label) {
	      return this.$label.html(this.limit - chars_count);
	    }
	  };

	  Limiter.DEFAULTS = {
	    label: null
	  };

	  $.fn.limiter = function(limit, options) {
	    return this.each(function() {
	      var $this;
	      $this = $(this);
	      if (!$this.attr('data-limiter')) {
	        return $.data($this, 'limiter', new Limiter($this, limit, options));
	      }
	    });
	  };

	  $.fn.limiter.Constructor = Limiter;

	}).call(this);
	;

	/*
	 * @class ExpandingInput
	 */

	(function() {
	  var ExpandingInput;

	  ExpandingInput = function($container, options) {
	    if (options == null) {
	      options = {};
	    }
	    this.options = $.extend({}, ExpandingInput.DEFAULTS, options || {});
	    this.$container = $container;
	    this.$target = this.options.target && this.$container.find(this.options.target).length ? this.$container.find(this.options.target) : null;
	    this.$content = this.options.hidden_content && this.$container.find(this.options.hidden_content).length ? this.$container.find(this.options.hidden_content) : null;
	    this.$container.addClass('expanding-input');
	    if (this.$target) {
	      this.$target.addClass('expanding-input-target');
	      if (this.$target.hasClass('input-sm')) {
	        this.$container.addClass('expanding-input-sm');
	      }
	      if (this.$target.hasClass('input-lg')) {
	        this.$container.addClass('expanding-input-lg');
	      }
	    }
	    if (this.$content) {
	      this.$content.addClass('expanding-input-content');
	    }
	    this.$overlay = $('<div class="expanding-input-overlay"></div>').appendTo(this.$container);
	    if (this.$target && this.$target.attr('placeholder')) {
	      if (!this.options.placeholder) {
	        this.options.placeholder = this.$target.attr('placeholder');
	      }
	      this.$target.attr('placeholder', '');
	    }
	    if (this.options.placeholder) {
	      this.$overlay.append($('<div class="expanding-input-placeholder"></div>').html(this.options.placeholder));
	    }
	    if (this.$target) {
	      this.$target.on('focus', $.proxy(this.expand, this));
	    }
	    return this.$overlay.on('click.expanding_input', $.proxy(this.expand, this));
	  };

	  ExpandingInput.prototype.expand = function() {
	    if (this.$container.hasClass('expanded')) {
	      return;
	    }
	    if (this.options.onBeforeExpand) {
	      this.options.onBeforeExpand.call(this);
	    }
	    this.$overlay.remove();
	    this.$container.addClass('expanded');
	    if (this.$target) {
	      setTimeout((function(_this) {
	        return function() {
	          return _this.$target.focus();
	        };
	      })(this), 1);
	    }
	    if (this.$target && this.options.placeholder) {
	      this.$target.attr('placeholder', $('<div>' + this.options.placeholder + '</div>').text());
	    }
	    if (this.options.onAfterExpand) {
	      return this.options.onAfterExpand.call(this);
	    }
	  };

	  ExpandingInput.DEFAULTS = {
	    target: null,
	    hidden_content: null,
	    placeholder: null,
	    onBeforeExpand: null,
	    onAfterExpand: null
	  };

	  $.fn.expandingInput = function(options) {
	    return this.each(function() {
	      var $this;
	      $this = $(this);
	      if (!$this.attr('data-expandingInput')) {
	        return $.data($this, 'expandingInput', new ExpandingInput($this, options));
	      }
	    });
	  };

	  $.fn.expandingInput.Constructor = ExpandingInput;

	}).call(this);
	;
	(function() {
	  var Wizard, setElementWidth;

	  setElementWidth = function($el, width) {
	    return $el.css({
	      'width': width,
	      'max-width': width,
	      'min-width': width
	    });
	  };


	  /*
	   * @class Wizard
	   */

	  Wizard = function($element, options) {
	    var $cur_step, self;
	    if (options == null) {
	      options = {};
	    }
	    this.options = $.extend({}, Wizard.DEFAULTS, options || {});
	    this.$element = $element;
	    this.$wrapper = $('.wizard-wrapper', this.$element);
	    this.$steps = $('.wizard-steps', this.$wrapper);
	    this.$step_items = $('> li', this.$steps);
	    this.steps_count = this.$step_items.length;
	    this.$content = $('.wizard-content', this.$element);
	    this.current_step = null;
	    this.$current_item = null;
	    this.isFreeze = false;
	    this.isRtl = $('body').hasClass('right-to-left');
	    this.resizeStepItems();
	    $cur_step = $('> li.active', this.$steps);
	    if ($cur_step.length === 0) {
	      $cur_step = $('> li:first-child', this.$steps);
	    }
	    this.$current_item = $cur_step.addClass('active');
	    this.current_step = $cur_step.index() + 1;
	    $($cur_step.attr('data-target'), this.$content).css({
	      display: 'block'
	    });
	    this.placeStepItems();
	    this._setPrevItemStates(this.current_step - 1);
	    $(window).on('pa.resize.wizard', (function(_this) {
	      return function() {
	        $.proxy(_this.resizeStepItems, _this)();
	        return $.proxy(_this.placeStepItems, _this)();
	      };
	    })(this));
	    self = this;
	    this.$steps.on('click', '> li', function() {
	      var $this;
	      $this = $(this);
	      if ($this.hasClass('completed')) {
	        return self.setCurrentStep($this.index() + 1);
	      }
	    });
	    return this;
	  };

	  Wizard.DEFAULTS = {
	    step_item_min_width: 200,
	    onChange: null,
	    onFinish: null
	  };


	  /*
	   * Resize steps
	   */

	  Wizard.prototype.resizeStepItems = function() {
	    var width;
	    if (this.$element.width() > (Wizard.DEFAULTS.step_item_min_width * this.$step_items.length)) {
	      width = Math.floor(this.$element.width() / this.$step_items.length);
	    } else {
	      width = Wizard.DEFAULTS.step_item_min_width;
	    }
	    return this.$step_items.each(function() {
	      return setElementWidth($(this), width);
	    });
	  };


	  /*
	   * Move steps
	   */

	  Wizard.prototype.placeStepItems = function() {
	    var current_item_width, d, element_width, item_position, item_position_right, offset, steps_width;
	    offset = 0;
	    item_position = this.$current_item.position();
	    element_width = this.$element.outerWidth();
	    steps_width = this.$steps.outerWidth();
	    current_item_width = this.$current_item.outerWidth();
	    d = (element_width - current_item_width) / 2;
	    if (!this.isRtl) {
	      if (element_width < steps_width && item_position.left > d) {
	        offset = -1 * item_position.left + d;
	        if ((steps_width + offset) < element_width) {
	          offset = -1 * steps_width + element_width;
	        }
	      }
	      return this.$steps.css({
	        left: offset
	      });
	    } else {
	      item_position_right = steps_width - item_position.left - current_item_width;
	      if (element_width < steps_width && item_position_right > d) {
	        offset = -1 * item_position_right + d;
	        if ((steps_width + offset) < element_width) {
	          offset = -1 * steps_width + element_width;
	        }
	      }
	      return this.$steps.css({
	        right: offset
	      });
	    }
	  };


	  /*
	   * Change step
	   *
	   * @param {Integer} step
	   */

	  Wizard.prototype.setCurrentStep = function(step) {
	    if (this.isFreeze) {
	      return;
	    }
	    return $(this.$current_item.attr('data-target'), this.$content).css({
	      opacity: 1
	    }).animate({
	      opacity: 0
	    }, 200, (function(_this) {
	      return function() {
	        $(_this.$current_item.attr('data-target'), _this.$content).attr('style', '');
	        _this._clearItemStates(step - 1);
	        _this.$current_item = _this.$step_items.eq(step - 1).addClass('active');
	        _this.current_step = step;
	        _this._setPrevItemStates(step - 1);
	        return $(_this.$current_item.attr('data-target'), _this.$content).css({
	          display: 'block',
	          opacity: 0
	        }).animate({
	          opacity: 1
	        }, 200, function() {
	          if (_this.options.onChange) {
	            $.proxy(_this.options.onChange, _this)();
	          }
	          return _this.placeStepItems();
	        });
	      };
	    })(this));
	  };


	  /*
	   * Move to next step
	   */

	  Wizard.prototype.nextStep = function() {
	    if (this.isFreeze) {
	      return;
	    }
	    if (this.current_step >= this.steps_count) {
	      if (this.options.onFinish) {
	        $.proxy(this.options.onFinish, this)();
	      }
	      return;
	    }
	    this.$current_item.removeClass('active').addClass('completed');
	    return this.setCurrentStep(this.current_step + 1);
	  };


	  /*
	   * Move to previous step
	   */

	  Wizard.prototype.prevStep = function() {
	    if (this.isFreeze) {
	      return;
	    }
	    if (this.current_step <= 1) {
	      return;
	    }
	    return this.setCurrentStep(this.current_step - 1);
	  };


	  /*
	   * Get current step
	   */

	  Wizard.prototype.currentStep = function() {
	    return this.current_step;
	  };


	  /*
	   * @param {Integer} start_index
	   */

	  Wizard.prototype._clearItemStates = function(start_index) {
	    var i, _i, _ref, _results;
	    _results = [];
	    for (i = _i = start_index, _ref = this.steps_count; start_index <= _ref ? _i < _ref : _i > _ref; i = start_index <= _ref ? ++_i : --_i) {
	      _results.push(this.$step_items.eq(i).removeClass('active').removeClass('completed'));
	    }
	    return _results;
	  };


	  /*
	   * @param {Integer} end_index
	   */

	  Wizard.prototype._setPrevItemStates = function(end_index) {
	    var i, _i, _results;
	    _results = [];
	    for (i = _i = 0; 0 <= end_index ? _i < end_index : _i > end_index; i = 0 <= end_index ? ++_i : --_i) {
	      _results.push(this.$step_items.eq(i).addClass('completed'));
	    }
	    return _results;
	  };


	  /*
	   * Freeze wizard
	   */

	  Wizard.prototype.freeze = function() {
	    this.isFreeze = true;
	    return this.$element.addClass('freeze');
	  };


	  /*
	   * Unfreeze wizard
	   */

	  Wizard.prototype.unfreeze = function() {
	    this.isFreeze = false;
	    return this.$element.removeClass('freeze');
	  };


	  /*
	   * jQuery Wizard plugin initialization
	   *
	   * @param {Object|String} options
	   * @param {Object} attrs
	   */

	  $.fn.pixelWizard = function(options, attrs) {
	    var $set, methodReturn;
	    methodReturn = void 0;
	    $set = this.each(function() {
	      var $this, wizard_obj;
	      $this = $(this);
	      if (!$.data(this, 'pixelWizard')) {
	        return $.data(this, 'pixelWizard', new Wizard($this, options));
	      } else if (options && typeof options === 'string') {
	        wizard_obj = $.data(this, 'pixelWizard');
	        if (options === 'setCurrentStep') {
	          return wizard_obj.setCurrentStep(attrs);
	        } else if (options === 'currentStep') {
	          return methodReturn = wizard_obj.currentStep();
	        } else if (options === 'nextStep') {
	          return wizard_obj.nextStep();
	        } else if (options === 'prevStep') {
	          return wizard_obj.prevStep();
	        } else if (options === 'freeze') {
	          return wizard_obj.freeze();
	        } else if (options === 'unfreeze') {
	          return wizard_obj.unfreeze();
	        } else if (options === 'resizeSteps') {
	          return wizard_obj.resizeStepItems();
	        }
	      }
	    });
	    return (methodReturn === void 0 ? $set : methodReturn);
	  };

	  $.fn.pixelWizard.Constructor = Wizard;

	}).call(this);
	;

	/*
	 * @class FileInput
	 */

	(function() {
	  var FileInput;

	  FileInput = function($input, options) {
	    if (options == null) {
	      options = {};
	    }
	    this.options = $.extend({}, FileInput.DEFAULTS, options || {});
	    this.$input = $input;
	    this.$el = $('<div class="pixel-file-input"><span class="pfi-filename"></span><div class="pfi-actions"></div></div>').insertAfter($input).append($input);
	    this.$filename = $('.pfi-filename', this.$el);
	    this.$clear_btn = $(this.options.clear_btn_tmpl).addClass('pfi-clear').appendTo($('.pfi-actions', this.$el));
	    this.$choose_btn = $(this.options.choose_btn_tmpl).addClass('pfi-choose').appendTo($('.pfi-actions', this.$el));
	    this.onChange();
	    $input.on('change', (function(_this) {
	      return function() {
	        return $.proxy(_this.onChange, _this)();
	      };
	    })(this)).on('click', function(e) {
	      return e.stopPropagation();
	    });
	    this.$el.on('click', function() {
	      return $input.click();
	    });
	    this.$choose_btn.on('click', function(e) {
	      return e.preventDefault();
	    });
	    return this.$clear_btn.on('click', (function(_this) {
	      return function(e) {
	        $input.wrap('<form>').parent('form').trigger('reset');
	        $input.unwrap();
	        $.proxy(_this.onChange, _this)();
	        e.stopPropagation();
	        return e.preventDefault();
	      };
	    })(this));
	  };

	  FileInput.DEFAULTS = {
	    choose_btn_tmpl: '<a href="#" class="btn btn-xs btn-primary">Choose</a>',
	    clear_btn_tmpl: '<a href="#" class="btn btn-xs"><i class="fa fa-times"></i> Clear</a>',
	    placeholder: null
	  };

	  FileInput.prototype.onChange = function() {
	    var value;
	    value = this.$input.val().replace(/\\/g, '/');
	    if (value !== '') {
	      this.$clear_btn.css('display', 'inline-block');
	      this.$filename.removeClass('pfi-placeholder');
	      return this.$filename.text(value.split('/').pop());
	    } else {
	      this.$clear_btn.css('display', 'none');
	      if (this.options.placeholder) {
	        this.$filename.addClass('pfi-placeholder');
	        return this.$filename.text(this.options.placeholder);
	      } else {
	        return this.$filename.text('');
	      }
	    }
	  };

	  $.fn.pixelFileInput = function(options) {
	    return this.each(function() {
	      if (!$.data(this, 'pixelFileInput')) {
	        return $.data(this, 'pixelFileInput', new FileInput($(this), options));
	      }
	    });
	  };

	  $.fn.pixelFileInput.Constructor = FileInput;

	}).call(this);
	;
	(function() {
	  var Tasks;

	  Tasks = function($el, options) {
	    var self;
	    if (options == null) {
	      options = {};
	    }
	    this.$el = $el;
	    this.options = $.extend({}, Tasks.DEFAULTS, options);
	    self = this;
	    $el.on('click', '.task input[type=checkbox]', function() {
	      return self.completeTask($(this));
	    }).on('click', '.task a', function() {
	      $(this).parents('.task').find('input[type=checkbox]').click();
	      return false;
	    });
	    return this;
	  };

	  Tasks.prototype.completeTask = function($trigger) {
	    var $task;
	    $task = $trigger.parents('.task');
	    if ($task.toggleClass('completed').hasClass('completed')) {
	      return this.options.onComplete.call(this);
	    } else {
	      return this.options.onCancel.call(this);
	    }
	  };

	  Tasks.prototype.clearCompletedTasks = function() {
	    $('.completed', this.$el).hide(200, function() {
	      return $(this).remove();
	    });
	    return this.options.onClear.call(this);
	  };

	  Tasks.DEFAULTS = {
	    onComplete: function() {},
	    onCancel: function() {},
	    onClear: function() {}
	  };

	  $.fn.pixelTasks = function(options) {
	    return $(this).each(function() {
	      var $this, pt;
	      $this = $(this);
	      pt = $.data(this, 'pixelTasks');
	      if (!pt) {
	        return $.data(this, 'pixelTasks', new Tasks($this, options));
	      } else if (options === 'clearCompletedTasks') {
	        return pt.clearCompletedTasks();
	      }
	    });
	  };

	  $.fn.pixelTasks.Constructor = Tasks;

	}).call(this);
	;
	(function() {
	  var Rating;

	  Rating = function($el, options) {
	    var i, self, _i, _ref;
	    if (options == null) {
	      options = {};
	    }
	    this.options = $.extend({}, Rating.DEFAULTS, options);
	    this.$container = $('<ul class="widget-rating"></ul>');
	    for (i = _i = 0, _ref = this.options.stars_count; 0 <= _ref ? _i < _ref : _i > _ref; i = 0 <= _ref ? ++_i : --_i) {
	      this.$container.append($('<li><a href="#" title="" class="widget-rating-item"></a></li>'));
	    }
	    $el.append(this.$container);
	    self = this;
	    this.$container.find('a').on('mouseenter', function() {
	      self.$container.find('li').removeClass(self.options.class_active);
	      return $(this).parents('li').addClass(self.options.class_active).prevAll('li').addClass(self.options.class_active);
	    }).on('mouseleave', function() {
	      return self.setRating(self.options.rating);
	    }).on('click', function() {
	      self.options.onRatingChange.call(self, $(this).parents('li').prevAll('li').length + 1);
	      return false;
	    });
	    this.setRating(this.options.rating);
	    return this;
	  };

	  Rating.prototype.setRating = function(value) {
	    this.options.rating = value;
	    if ((value - Math.floor(value)) > this.options.lower_limit) {
	      value = Math.ceil(value);
	    } else {
	      value = Math.floor(value);
	    }
	    return this.$container.find('li').removeClass(this.options.class_active).slice(0, value).addClass(this.options.class_active);
	  };

	  Rating.DEFAULTS = {
	    stars_count: 5,
	    rating: 0,
	    class_active: 'active',
	    lower_limit: 0.35,
	    onRatingChange: function(value) {}
	  };

	  $.fn.pixelRating = function(options, args) {
	    return $(this).each(function() {
	      var $this, pr;
	      $this = $(this);
	      pr = $.data(this, 'pixelRating');
	      if (!pr) {
	        return $.data(this, 'pixelRating', new Rating($this, options));
	      } else if (options === 'setRating') {
	        return pr.setRating(args);
	      }
	    });
	  };

	  $.fn.pixelRating.Constructor = Rating;

	}).call(this);
	;

	(function($) {

	  jQuery.fn.extend({
	    pixelSlimScroll: function(options) {

	      var defaults = {

	        // width in pixels of the visible scroll area
	        width : 'auto',

	        // width in pixels of the scrollbar and rail
	        size : '2px',

	        // scrollbar color, accepts any hex/color value
	        color: '#000',

	        // distance in pixels between the side edge and the scrollbar
	        distance : '1px',

	        // default scroll position on load - top / bottom / $('selector')
	        start : 'top',

	        // sets scrollbar opacity
	        opacity : .4,

	        // sets rail color
	        railColor : '#333',

	        // sets rail opacity
	        railOpacity : .2,

	        // defautlt CSS class of the slimscroll rail
	        railClass : 'slimScrollRail',

	        // defautlt CSS class of the slimscroll bar
	        barClass : 'slimScrollBar',

	        // defautlt CSS class of the slimscroll wrapper
	        wrapperClass : 'slimScrollDiv',

	        // check if mousewheel should scroll the window if we reach top/bottom
	        allowPageScroll : false,

	        // scroll amount applied to each mouse wheel step
	        wheelStep : 20,

	        // scroll amount applied when user is using gestures
	        touchScrollStep : 200,

	        // sets border radius
	        borderRadius: '0px',

	        // sets border radius of the rail
	        railBorderRadius : '0px'
	      };

	      var o = $.extend(defaults, options);

	      // do it for every element that matches selector
	      this.each(function(){

	      var isOverPanel, isOverBar, isDragg, queueHide, touchDif,
	        barHeight, percentScroll, lastScroll,
	        divS = '<div></div>',
	        minBarHeight = 30,
	        releaseScroll = false;

	        // used in event handlers and for better minification
	        var me = $(this);

	        // ensure we are not binding it again
	        if (me.parent().hasClass(o.wrapperClass))
	        {
	            // start from last bar position
	            var offset = me.scrollTop();

	            // find bar and rail
	            bar = me.parent().find('.' + o.barClass);
	            rail = me.parent().find('.' + o.railClass);

	            getBarHeight();

	            // check if we should scroll existing instance
	            if ($.isPlainObject(options))
	            {
	              if ('scrollTo' in options)
	              {
	                // jump to a static point
	                offset = parseInt(o.scrollTo);
	              }
	              else if ('scrollBy' in options)
	              {
	                // jump by value pixels
	                offset += parseInt(o.scrollBy);
	              }
	              else if ('destroy' in options)
	              {
	                // remove slimscroll elements
	                bar.remove();
	                rail.remove();
	                me.unwrap();
	                return;
	              }

	              // scroll content by the given offset
	              scrollContent(offset, false, true);
	            }

	            return;
	        }

	        // wrap content
	        var wrapper = $(divS)
	          .addClass(o.wrapperClass)
	          .css({
	            position: 'relative',
	            overflow: 'hidden',
	            width: o.width
	          });

	        // update style for the div
	        me.css({
	          overflow: 'hidden',
	          width: o.width
	        });

	        // create scrollbar rail
	        var rail = $(divS)
	          .addClass(o.railClass)
	          .css({
	            width: o.size,
	            height: '100%',
	            position: 'absolute',
	            top: 0,
	            display: 'none',
	            'border-radius': o.railBorderRadius,
	            background: o.railColor,
	            opacity: o.railOpacity,
	            zIndex: 90
	          });

	        // create scrollbar
	        var bar = $(divS)
	          .addClass(o.barClass)
	          .css({
	            background: o.color,
	            width: o.size,
	            position: 'absolute',
	            top: 0,
	            opacity: o.opacity,
	            display: 'block',
	            'border-radius' : o.borderRadius,
	            BorderRadius: o.borderRadius,
	            MozBorderRadius: o.borderRadius,
	            WebkitBorderRadius: o.borderRadius,
	            zIndex: 99
	          });

	        // set position
	        rail.css({ right: o.distance });
	        bar.css({ right: o.distance });

	        // wrap it
	        me.wrap(wrapper);

	        // append to parent div
	        me.parent().append(bar);
	        me.parent().append(rail);

	        // make it draggable and no longer dependent on the jqueryUI
	        bar.bind("mousedown", function(e) {
	          var $doc = $(document);
	          isDragg = true;
	          t = parseFloat(bar.css('top'));
	          pageY = e.pageY;

	          $doc.bind("mousemove.slimscroll", function(e){
	            currTop = t + e.pageY - pageY;
	            bar.css('top', currTop);
	            scrollContent(0, bar.position().top, false);// scroll content
	          });

	          $doc.bind("mouseup.slimscroll", function(e) {
	            isDragg = false;hideBar();
	            $doc.unbind('.slimscroll');
	          });
	          return false;
	        }).bind("selectstart.slimscroll", function(e){
	          e.stopPropagation();
	          e.preventDefault();
	          return false;
	        });

	        // on rail over
	        rail.hover(function(){
	          showBar();
	        }, function(){
	          hideBar();
	        });

	        // on bar over
	        bar.hover(function(){
	          isOverBar = true;
	        }, function(){
	          isOverBar = false;
	        });

	        // show on parent mouseover
	        me.hover(function(){
	          isOverPanel = true;
	          showBar();
	          hideBar();
	        }, function(){
	          isOverPanel = false;
	          hideBar();
	        });

	        // support for mobile
	        me.bind('touchstart', function(e,b){
	          if (e.originalEvent.touches.length)
	          {
	            // record where touch started
	            touchDif = e.originalEvent.touches[0].pageY;
	          }
	        });

	        me.bind('touchmove', function(e){
	          // prevent scrolling the page if necessary
	          if(!releaseScroll)
	          {
	  		      e.originalEvent.preventDefault();
			      }
	          if (e.originalEvent.touches.length)
	          {
	            // see how far user swiped
	            var diff = (touchDif - e.originalEvent.touches[0].pageY) / o.touchScrollStep;
	            // scroll content
	            scrollContent(diff, true);
	            touchDif = e.originalEvent.touches[0].pageY;
	          }
	        });

	        // set up initial height
	        getBarHeight();

	        // attach scroll events
	        attachWheel();

	        function _onWheel(e)
	        {
	          // use mouse wheel only when mouse is over
	          if (!isOverPanel) { return; }

	          var e = e || window.event;

	          var delta = 0;
	          if (e.wheelDelta) { delta = -e.wheelDelta/120; }
	          if (e.detail) { delta = e.detail / 3; }

	          var target = e.target || e.srcTarget || e.srcElement;
	          if ($(target).closest('.' + o.wrapperClass).is(me.parent())) {
	            // scroll content
	            scrollContent(delta, true);
	          }

	          // stop window scroll
	          if (e.preventDefault && !releaseScroll) { e.preventDefault(); }
	          if (!releaseScroll) { e.returnValue = false; }
	        }

	        function scrollContent(y, isWheel, isJump)
	        {
	          releaseScroll = false;
	          var delta = y;
	          var maxTop = me.outerHeight() - bar.outerHeight();

	          if (isWheel)
	          {
	            // move bar with mouse wheel
	            delta = parseInt(bar.css('top')) + y * parseInt(o.wheelStep) / 100 * bar.outerHeight();

	            // move bar, make sure it doesn't go out
	            delta = Math.min(Math.max(delta, 0), maxTop);

	            // if scrolling down, make sure a fractional change to the
	            // scroll position isn't rounded away when the scrollbar's CSS is set
	            // this flooring of delta would happened automatically when
	            // bar.css is set below, but we floor here for clarity
	            delta = (y > 0) ? Math.ceil(delta) : Math.floor(delta);

	            // scroll the scrollbar
	            bar.css({ top: delta + 'px' });
	          }

	          // calculate actual scroll amount
	          percentScroll = parseInt(bar.css('top')) / (me.outerHeight() - bar.outerHeight());
	          delta = percentScroll * (me[0].scrollHeight - me.outerHeight());

	          if (isJump)
	          {
	            delta = y;
	            var offsetTop = delta / me[0].scrollHeight * me.outerHeight();
	            offsetTop = Math.min(Math.max(offsetTop, 0), maxTop);
	            bar.css({ top: offsetTop + 'px' });
	          }

	          // scroll content
	          me.scrollTop(delta);

	          // fire scrolling event
	          me.trigger('slimscrolling', ~~delta);

	          // ensure bar is visible
	          showBar();

	          // trigger hide when scroll is stopped
	          hideBar();
	        }

	        function attachWheel()
	        {
	          if (window.addEventListener)
	          {
	            this.addEventListener('DOMMouseScroll', _onWheel, false );
	            this.addEventListener('mousewheel', _onWheel, false );
	          }
	          else
	          {
	            document.attachEvent("onmousewheel", _onWheel)
	          }
	        }

	        function getBarHeight()
	        {
	          // calculate scrollbar height and make sure it is not too small
	          barHeight = Math.max((me.outerHeight() / me[0].scrollHeight) * me.outerHeight(), minBarHeight);
	          bar.css({ height: barHeight + 'px' });

	          // hide scrollbar if content is not long enough
	          var display = barHeight == me.outerHeight() ? 'none' : 'block';
	          bar.css({ display: display });
	        }

	        function showBar()
	        {
	          // recalculate bar height
	          getBarHeight();
	          clearTimeout(queueHide);

	          // when bar reached top or bottom
	          if (percentScroll == ~~percentScroll)
	          {
	            //release wheel
	            releaseScroll = o.allowPageScroll;

	            // publish approporiate event
	            if (lastScroll != percentScroll)
	            {
	                var msg = (~~percentScroll == 0) ? 'top' : 'bottom';
	                me.trigger('slimscroll', msg);
	            }
	          }
	          else
	          {
	            releaseScroll = false;
	          }
	          lastScroll = percentScroll;

	          // show only when required
	          if(barHeight >= me.outerHeight()) {
	            //allow window scroll
	            releaseScroll = true;
	            return;
	          }
	          bar.stop(true,true).fadeIn('fast');
	        }

	        function hideBar() { }

	      });

	      // maintain chainability
	      return this;
	    }
	  });

	  jQuery.fn.extend({
	    pixelslimscroll: jQuery.fn.pixelSlimScroll
	  });

	})(jQuery);
	;
	/* =========================================================
	 * bootstrap-datepicker.js
	 * Repo: https://github.com/eternicode/bootstrap-datepicker/
	 * Demo: http://eternicode.github.io/bootstrap-datepicker/
	 * Docs: http://bootstrap-datepicker.readthedocs.org/
	 * Forked from http://www.eyecon.ro/bootstrap-datepicker
	 * =========================================================
	 * Started by Stefan Petre; improvements by Andrew Rowls + contributors
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 * ========================================================= */

	(function($, undefined){

		var $window = $(window);

		function UTCDate(){
			return new Date(Date.UTC.apply(Date, arguments));
		}
		function UTCToday(){
			var today = new Date();
			return UTCDate(today.getFullYear(), today.getMonth(), today.getDate());
		}
		function alias(method){
			return function(){
				return this[method].apply(this, arguments);
			};
		}

		var DateArray = (function(){
			var extras = {
				get: function(i){
					return this.slice(i)[0];
				},
				contains: function(d){
					// Array.indexOf is not cross-browser;
					// $.inArray doesn't work with Dates
					var val = d && d.valueOf();
					for (var i=0, l=this.length; i < l; i++)
						if (this[i].valueOf() === val)
							return i;
					return -1;
				},
				remove: function(i){
					this.splice(i,1);
				},
				replace: function(new_array){
					if (!new_array)
						return;
					if (!$.isArray(new_array))
						new_array = [new_array];
					this.clear();
					this.push.apply(this, new_array);
				},
				clear: function(){
					this.splice(0);
				},
				copy: function(){
					var a = new DateArray();
					a.replace(this);
					return a;
				}
			};

			return function(){
				var a = [];
				a.push.apply(a, arguments);
				$.extend(a, extras);
				return a;
			};
		})();


		// Picker object

		var Datepicker = function(element, options){
			this.dates = new DateArray();
			this.viewDate = UTCToday();
			this.focusDate = null;

			this._process_options(options);

			this.element = $(element);
			this.isInline = false;
			this.isInput = this.element.is('input');
			this.component = this.element.is('.date') ? this.element.find('.add-on, .input-group-addon, .btn') : false;
			this.hasInput = this.component && this.element.find('input').length;
			if (this.component && this.component.length === 0)
				this.component = false;

			this.picker = $(DPGlobal.template);
			this._buildEvents();
			this._attachEvents();

			if (this.isInline){
				this.picker.addClass('datepicker-inline').appendTo(this.element);
			}
			else {
				this.picker.addClass('datepicker-dropdown dropdown-menu');
			}

			if (this.o.rtl){
				this.picker.addClass('datepicker-rtl');
			}

			this.viewMode = this.o.startView;

			if (this.o.calendarWeeks)
				this.picker.find('tfoot th.today')
							.attr('colspan', function(i, val){
								return parseInt(val) + 1;
							});

			this._allow_update = false;

			this.setStartDate(this._o.startDate);
			this.setEndDate(this._o.endDate);
			this.setDaysOfWeekDisabled(this.o.daysOfWeekDisabled);

			this.fillDow();
			this.fillMonths();

			this._allow_update = true;

			this.update();
			this.showMode();

			if (this.isInline){
				this.show();
			}
		};

		Datepicker.prototype = {
			constructor: Datepicker,

			_process_options: function(opts){
				// Store raw options for reference
				this._o = $.extend({}, this._o, opts);
				// Processed options
				var o = this.o = $.extend({}, this._o);

				// Check if "de-DE" style date is available, if not language should
				// fallback to 2 letter code eg "de"
				var lang = o.language;
				if (!dates[lang]){
					lang = lang.split('-')[0];
					if (!dates[lang])
						lang = defaults.language;
				}
				o.language = lang;

				switch (o.startView){
					case 2:
					case 'decade':
						o.startView = 2;
						break;
					case 1:
					case 'year':
						o.startView = 1;
						break;
					default:
						o.startView = 0;
				}

				switch (o.minViewMode){
					case 1:
					case 'months':
						o.minViewMode = 1;
						break;
					case 2:
					case 'years':
						o.minViewMode = 2;
						break;
					default:
						o.minViewMode = 0;
				}

				o.startView = Math.max(o.startView, o.minViewMode);

				// true, false, or Number > 0
				if (o.multidate !== true){
					o.multidate = Number(o.multidate) || false;
					if (o.multidate !== false)
						o.multidate = Math.max(0, o.multidate);
					else
						o.multidate = 1;
				}
				o.multidateSeparator = String(o.multidateSeparator);

				o.weekStart %= 7;
				o.weekEnd = ((o.weekStart + 6) % 7);

				var format = DPGlobal.parseFormat(o.format);
				if (o.startDate !== -Infinity){
					if (!!o.startDate){
						if (o.startDate instanceof Date)
							o.startDate = this._local_to_utc(this._zero_time(o.startDate));
						else
							o.startDate = DPGlobal.parseDate(o.startDate, format, o.language);
					}
					else {
						o.startDate = -Infinity;
					}
				}
				if (o.endDate !== Infinity){
					if (!!o.endDate){
						if (o.endDate instanceof Date)
							o.endDate = this._local_to_utc(this._zero_time(o.endDate));
						else
							o.endDate = DPGlobal.parseDate(o.endDate, format, o.language);
					}
					else {
						o.endDate = Infinity;
					}
				}

				o.daysOfWeekDisabled = o.daysOfWeekDisabled||[];
				if (!$.isArray(o.daysOfWeekDisabled))
					o.daysOfWeekDisabled = o.daysOfWeekDisabled.split(/[,\s]*/);
				o.daysOfWeekDisabled = $.map(o.daysOfWeekDisabled, function(d){
					return parseInt(d, 10);
				});

				var plc = String(o.orientation).toLowerCase().split(/\s+/g),
					_plc = o.orientation.toLowerCase();
				plc = $.grep(plc, function(word){
					return (/^auto|left|right|top|bottom$/).test(word);
				});
				o.orientation = {x: 'auto', y: 'auto'};
				if (!_plc || _plc === 'auto')
					; // no action
				else if (plc.length === 1){
					switch (plc[0]){
						case 'top':
						case 'bottom':
							o.orientation.y = plc[0];
							break;
						case 'left':
						case 'right':
							o.orientation.x = plc[0];
							break;
					}
				}
				else {
					_plc = $.grep(plc, function(word){
						return (/^left|right$/).test(word);
					});
					o.orientation.x = _plc[0] || 'auto';

					_plc = $.grep(plc, function(word){
						return (/^top|bottom$/).test(word);
					});
					o.orientation.y = _plc[0] || 'auto';
				}
			},
			_events: [],
			_secondaryEvents: [],
			_applyEvents: function(evs){
				for (var i=0, el, ch, ev; i < evs.length; i++){
					el = evs[i][0];
					if (evs[i].length === 2){
						ch = undefined;
						ev = evs[i][1];
					}
					else if (evs[i].length === 3){
						ch = evs[i][1];
						ev = evs[i][2];
					}
					el.on(ev, ch);
				}
			},
			_unapplyEvents: function(evs){
				for (var i=0, el, ev, ch; i < evs.length; i++){
					el = evs[i][0];
					if (evs[i].length === 2){
						ch = undefined;
						ev = evs[i][1];
					}
					else if (evs[i].length === 3){
						ch = evs[i][1];
						ev = evs[i][2];
					}
					el.off(ev, ch);
				}
			},
			_buildEvents: function(){
				if (this.isInput){ // single input
					this._events = [
						[this.element, {
							focus: $.proxy(this.show, this),
							keyup: $.proxy(function(e){
								if ($.inArray(e.keyCode, [27,37,39,38,40,32,13,9]) === -1)
									this.update();
							}, this),
							keydown: $.proxy(this.keydown, this)
						}]
					];
				}
				else if (this.component && this.hasInput){ // component: input + button
					this._events = [
						// For components that are not readonly, allow keyboard nav
						[this.element.find('input'), {
							focus: $.proxy(this.show, this),
							keyup: $.proxy(function(e){
								if ($.inArray(e.keyCode, [27,37,39,38,40,32,13,9]) === -1)
									this.update();
							}, this),
							keydown: $.proxy(this.keydown, this)
						}],
						[this.component, {
							click: $.proxy(this.show, this)
						}]
					];
				}
				else if (this.element.is('div')){  // inline datepicker
					this.isInline = true;
				}
				else {
					this._events = [
						[this.element, {
							click: $.proxy(this.show, this)
						}]
					];
				}
				this._events.push(
					// Component: listen for blur on element descendants
					[this.element, '*', {
						blur: $.proxy(function(e){
							this._focused_from = e.target;
						}, this)
					}],
					// Input: listen for blur on element
					[this.element, {
						blur: $.proxy(function(e){
							this._focused_from = e.target;
						}, this)
					}]
				);

				this._secondaryEvents = [
					[this.picker, {
						click: $.proxy(this.click, this)
					}],
					[$(window), {
						resize: $.proxy(this.place, this)
					}],
					[$(document), {
						'mousedown touchstart': $.proxy(function(e){
							// Clicked outside the datepicker, hide it
							if (!(
								this.element.is(e.target) ||
								this.element.find(e.target).length ||
								this.picker.is(e.target) ||
								this.picker.find(e.target).length
							)){
								this.hide();
							}
						}, this)
					}]
				];
			},
			_attachEvents: function(){
				this._detachEvents();
				this._applyEvents(this._events);
			},
			_detachEvents: function(){
				this._unapplyEvents(this._events);
			},
			_attachSecondaryEvents: function(){
				this._detachSecondaryEvents();
				this._applyEvents(this._secondaryEvents);
			},
			_detachSecondaryEvents: function(){
				this._unapplyEvents(this._secondaryEvents);
			},
			_trigger: function(event, altdate){
				var date = altdate || this.dates.get(-1),
					local_date = this._utc_to_local(date);

				this.element.trigger({
					type: event,
					date: local_date,
					dates: $.map(this.dates, this._utc_to_local),
					format: $.proxy(function(ix, format){
						if (arguments.length === 0){
							ix = this.dates.length - 1;
							format = this.o.format;
						}
						else if (typeof ix === 'string'){
							format = ix;
							ix = this.dates.length - 1;
						}
						format = format || this.o.format;
						var date = this.dates.get(ix);
						return DPGlobal.formatDate(date, format, this.o.language);
					}, this)
				});
			},

			show: function(){
				if (!this.isInline)
					this.picker.appendTo('body');
				this.picker.show();
				this.place();
				this._attachSecondaryEvents();
				this._trigger('show');
			},

			hide: function(){
				if (this.isInline)
					return;
				if (!this.picker.is(':visible'))
					return;
				this.focusDate = null;
				this.picker.hide().detach();
				this._detachSecondaryEvents();
				this.viewMode = this.o.startView;
				this.showMode();

				if (
					this.o.forceParse &&
					(
						this.isInput && this.element.val() ||
						this.hasInput && this.element.find('input').val()
					)
				)
					this.setValue();
				this._trigger('hide');
			},

			remove: function(){
				this.hide();
				this._detachEvents();
				this._detachSecondaryEvents();
				this.picker.remove();
				delete this.element.data().datepicker;
				if (!this.isInput){
					delete this.element.data().date;
				}
			},

			_utc_to_local: function(utc){
				return utc && new Date(utc.getTime() + (utc.getTimezoneOffset()*60000));
			},
			_local_to_utc: function(local){
				return local && new Date(local.getTime() - (local.getTimezoneOffset()*60000));
			},
			_zero_time: function(local){
				return local && new Date(local.getFullYear(), local.getMonth(), local.getDate());
			},
			_zero_utc_time: function(utc){
				return utc && new Date(Date.UTC(utc.getUTCFullYear(), utc.getUTCMonth(), utc.getUTCDate()));
			},

			getDates: function(){
				return $.map(this.dates, this._utc_to_local);
			},

			getUTCDates: function(){
				return $.map(this.dates, function(d){
					return new Date(d);
				});
			},

			getDate: function(){
				return this._utc_to_local(this.getUTCDate());
			},

			getUTCDate: function(){
				return new Date(this.dates.get(-1));
			},

			setDates: function(){
				var args = $.isArray(arguments[0]) ? arguments[0] : arguments;
				this.update.apply(this, args);
				this._trigger('changeDate');
				this.setValue();
			},

			setUTCDates: function(){
				var args = $.isArray(arguments[0]) ? arguments[0] : arguments;
				this.update.apply(this, $.map(args, this._utc_to_local));
				this._trigger('changeDate');
				this.setValue();
			},

			setDate: alias('setDates'),
			setUTCDate: alias('setUTCDates'),

			setValue: function(){
				var formatted = this.getFormattedDate();
				if (!this.isInput){
					if (this.component){
						this.element.find('input').val(formatted).change();
					}
				}
				else {
					this.element.val(formatted).change();
				}
			},

			getFormattedDate: function(format){
				if (format === undefined)
					format = this.o.format;

				var lang = this.o.language;
				return $.map(this.dates, function(d){
					return DPGlobal.formatDate(d, format, lang);
				}).join(this.o.multidateSeparator);
			},

			setStartDate: function(startDate){
				this._process_options({startDate: startDate});
				this.update();
				this.updateNavArrows();
			},

			setEndDate: function(endDate){
				this._process_options({endDate: endDate});
				this.update();
				this.updateNavArrows();
			},

			setDaysOfWeekDisabled: function(daysOfWeekDisabled){
				this._process_options({daysOfWeekDisabled: daysOfWeekDisabled});
				this.update();
				this.updateNavArrows();
			},

			place: function(){
				if (this.isInline)
					return;
				var calendarWidth = this.picker.outerWidth(),
					calendarHeight = this.picker.outerHeight(),
					visualPadding = 10,
					windowWidth = $window.width(),
					windowHeight = $window.height(),
					scrollTop = $window.scrollTop();

				var zIndex = parseInt(this.element.parents().filter(function(){
						return $(this).css('z-index') !== 'auto';
					}).first().css('z-index'))+10;
				var offset = this.component ? this.component.parent().offset() : this.element.offset();
				var height = this.component ? this.component.outerHeight(true) : this.element.outerHeight(false);
				var width = this.component ? this.component.outerWidth(true) : this.element.outerWidth(false);
				var left = offset.left,
					top = offset.top;

				this.picker.removeClass(
					'datepicker-orient-top datepicker-orient-bottom '+
					'datepicker-orient-right datepicker-orient-left'
				);

				if (this.o.orientation.x !== 'auto'){
					this.picker.addClass('datepicker-orient-' + this.o.orientation.x);
					if (this.o.orientation.x === 'right')
						left -= calendarWidth - width;
				}
				// auto x orientation is best-placement: if it crosses a window
				// edge, fudge it sideways
				else {
					// Default to left
					this.picker.addClass('datepicker-orient-left');
					if (offset.left < 0)
						left -= offset.left - visualPadding;
					else if (offset.left + calendarWidth > windowWidth)
						left = windowWidth - calendarWidth - visualPadding;
				}

				// auto y orientation is best-situation: top or bottom, no fudging,
				// decision based on which shows more of the calendar
				var yorient = this.o.orientation.y,
					top_overflow, bottom_overflow;
				if (yorient === 'auto'){
					top_overflow = -scrollTop + offset.top - calendarHeight;
					bottom_overflow = scrollTop + windowHeight - (offset.top + height + calendarHeight);
					if (Math.max(top_overflow, bottom_overflow) === bottom_overflow)
						yorient = 'top';
					else
						yorient = 'bottom';
				}
				this.picker.addClass('datepicker-orient-' + yorient);
				if (yorient === 'top')
					top += height;
				else
					top -= calendarHeight + parseInt(this.picker.css('padding-top'));

				this.picker.css({
					top: top,
					left: left,
					zIndex: zIndex
				});
			},

			_allow_update: true,
			update: function(){
				if (!this._allow_update)
					return;

				var oldDates = this.dates.copy(),
					dates = [],
					fromArgs = false;
				if (arguments.length){
					$.each(arguments, $.proxy(function(i, date){
						if (date instanceof Date)
							date = this._local_to_utc(date);
						dates.push(date);
					}, this));
					fromArgs = true;
				}
				else {
					dates = this.isInput
							? this.element.val()
							: this.element.data('date') || this.element.find('input').val();
					if (dates && this.o.multidate)
						dates = dates.split(this.o.multidateSeparator);
					else
						dates = [dates];
					delete this.element.data().date;
				}

				dates = $.map(dates, $.proxy(function(date){
					return DPGlobal.parseDate(date, this.o.format, this.o.language);
				}, this));
				dates = $.grep(dates, $.proxy(function(date){
					return (
						date < this.o.startDate ||
						date > this.o.endDate ||
						!date
					);
				}, this), true);
				this.dates.replace(dates);

				if (this.dates.length)
					this.viewDate = new Date(this.dates.get(-1));
				else if (this.viewDate < this.o.startDate)
					this.viewDate = new Date(this.o.startDate);
				else if (this.viewDate > this.o.endDate)
					this.viewDate = new Date(this.o.endDate);

				if (fromArgs){
					// setting date by clicking
					this.setValue();
				}
				else if (dates.length){
					// setting date by typing
					if (String(oldDates) !== String(this.dates))
						this._trigger('changeDate');
				}
				if (!this.dates.length && oldDates.length)
					this._trigger('clearDate');

				this.fill();
			},

			fillDow: function(){
				var dowCnt = this.o.weekStart,
					html = '<tr>';
				if (this.o.calendarWeeks){
					var cell = '<th class="cw">&nbsp;</th>';
					html += cell;
					this.picker.find('.datepicker-days thead tr:first-child').prepend(cell);
				}
				while (dowCnt < this.o.weekStart + 7){
					html += '<th class="dow">'+dates[this.o.language].daysMin[(dowCnt++)%7]+'</th>';
				}
				html += '</tr>';
				this.picker.find('.datepicker-days thead').append(html);
			},

			fillMonths: function(){
				var html = '',
				i = 0;
				while (i < 12){
					html += '<span class="month">'+dates[this.o.language].monthsShort[i++]+'</span>';
				}
				this.picker.find('.datepicker-months td').html(html);
			},

			setRange: function(range){
				if (!range || !range.length)
					delete this.range;
				else
					this.range = $.map(range, function(d){
						return d.valueOf();
					});
				this.fill();
			},

			getClassNames: function(date){
				var cls = [],
					year = this.viewDate.getUTCFullYear(),
					month = this.viewDate.getUTCMonth(),
					today = new Date();
				if (date.getUTCFullYear() < year || (date.getUTCFullYear() === year && date.getUTCMonth() < month)){
					cls.push('old');
				}
				else if (date.getUTCFullYear() > year || (date.getUTCFullYear() === year && date.getUTCMonth() > month)){
					cls.push('new');
				}
				if (this.focusDate && date.valueOf() === this.focusDate.valueOf())
					cls.push('focused');
				// Compare internal UTC date with local today, not UTC today
				if (this.o.todayHighlight &&
					date.getUTCFullYear() === today.getFullYear() &&
					date.getUTCMonth() === today.getMonth() &&
					date.getUTCDate() === today.getDate()){
					cls.push('today');
				}
				if (this.dates.contains(date) !== -1)
					cls.push('active');
				if (date.valueOf() < this.o.startDate || date.valueOf() > this.o.endDate ||
					$.inArray(date.getUTCDay(), this.o.daysOfWeekDisabled) !== -1){
					cls.push('disabled');
				}
				if (this.range){
					if (date > this.range[0] && date < this.range[this.range.length-1]){
						cls.push('range');
					}
					if ($.inArray(date.valueOf(), this.range) !== -1){
						cls.push('selected');
					}
				}
				return cls;
			},

			fill: function(){
				var d = new Date(this.viewDate),
					year = d.getUTCFullYear(),
					month = d.getUTCMonth(),
					startYear = this.o.startDate !== -Infinity ? this.o.startDate.getUTCFullYear() : -Infinity,
					startMonth = this.o.startDate !== -Infinity ? this.o.startDate.getUTCMonth() : -Infinity,
					endYear = this.o.endDate !== Infinity ? this.o.endDate.getUTCFullYear() : Infinity,
					endMonth = this.o.endDate !== Infinity ? this.o.endDate.getUTCMonth() : Infinity,
					todaytxt = dates[this.o.language].today || dates['en'].today || '',
					cleartxt = dates[this.o.language].clear || dates['en'].clear || '',
					tooltip;
				this.picker.find('.datepicker-days thead th.datepicker-switch')
							.text(dates[this.o.language].months[month]+' '+year);
				this.picker.find('tfoot th.today')
							.text(todaytxt)
							.toggle(this.o.todayBtn !== false);
				this.picker.find('tfoot th.clear')
							.text(cleartxt)
							.toggle(this.o.clearBtn !== false);
				this.updateNavArrows();
				this.fillMonths();
				var prevMonth = UTCDate(year, month-1, 28),
					day = DPGlobal.getDaysInMonth(prevMonth.getUTCFullYear(), prevMonth.getUTCMonth());
				prevMonth.setUTCDate(day);
				prevMonth.setUTCDate(day - (prevMonth.getUTCDay() - this.o.weekStart + 7)%7);
				var nextMonth = new Date(prevMonth);
				nextMonth.setUTCDate(nextMonth.getUTCDate() + 42);
				nextMonth = nextMonth.valueOf();
				var html = [];
				var clsName;
				while (prevMonth.valueOf() < nextMonth){
					if (prevMonth.getUTCDay() === this.o.weekStart){
						html.push('<tr>');
						if (this.o.calendarWeeks){
							// ISO 8601: First week contains first thursday.
							// ISO also states week starts on Monday, but we can be more abstract here.
							var
								// Start of current week: based on weekstart/current date
								ws = new Date(+prevMonth + (this.o.weekStart - prevMonth.getUTCDay() - 7) % 7 * 864e5),
								// Thursday of this week
								th = new Date(Number(ws) + (7 + 4 - ws.getUTCDay()) % 7 * 864e5),
								// First Thursday of year, year from thursday
								yth = new Date(Number(yth = UTCDate(th.getUTCFullYear(), 0, 1)) + (7 + 4 - yth.getUTCDay())%7*864e5),
								// Calendar week: ms between thursdays, div ms per day, div 7 days
								calWeek =  (th - yth) / 864e5 / 7 + 1;
							html.push('<td class="cw">'+ calWeek +'</td>');

						}
					}
					clsName = this.getClassNames(prevMonth);
					clsName.push('day');

					if (this.o.beforeShowDay !== $.noop){
						var before = this.o.beforeShowDay(this._utc_to_local(prevMonth));
						if (before === undefined)
							before = {};
						else if (typeof(before) === 'boolean')
							before = {enabled: before};
						else if (typeof(before) === 'string')
							before = {classes: before};
						if (before.enabled === false)
							clsName.push('disabled');
						if (before.classes)
							clsName = clsName.concat(before.classes.split(/\s+/));
						if (before.tooltip)
							tooltip = before.tooltip;
					}

					clsName = $.unique(clsName);
					html.push('<td class="'+clsName.join(' ')+'"' + (tooltip ? ' title="'+tooltip+'"' : '') + '>'+prevMonth.getUTCDate() + '</td>');
					if (prevMonth.getUTCDay() === this.o.weekEnd){
						html.push('</tr>');
					}
					prevMonth.setUTCDate(prevMonth.getUTCDate()+1);
				}
				this.picker.find('.datepicker-days tbody').empty().append(html.join(''));

				var months = this.picker.find('.datepicker-months')
							.find('th:eq(1)')
								.text(year)
								.end()
							.find('span').removeClass('active');

				$.each(this.dates, function(i, d){
					if (d.getUTCFullYear() === year)
						months.eq(d.getUTCMonth()).addClass('active');
				});

				if (year < startYear || year > endYear){
					months.addClass('disabled');
				}
				if (year === startYear){
					months.slice(0, startMonth).addClass('disabled');
				}
				if (year === endYear){
					months.slice(endMonth+1).addClass('disabled');
				}

				html = '';
				year = parseInt(year/10, 10) * 10;
				var yearCont = this.picker.find('.datepicker-years')
									.find('th:eq(1)')
										.text(year + '-' + (year + 9))
										.end()
									.find('td');
				year -= 1;
				var years = $.map(this.dates, function(d){
						return d.getUTCFullYear();
					}),
					classes;
				for (var i = -1; i < 11; i++){
					classes = ['year'];
					if (i === -1)
						classes.push('old');
					else if (i === 10)
						classes.push('new');
					if ($.inArray(year, years) !== -1)
						classes.push('active');
					if (year < startYear || year > endYear)
						classes.push('disabled');
					html += '<span class="' + classes.join(' ') + '">'+year+'</span>';
					year += 1;
				}
				yearCont.html(html);
			},

			updateNavArrows: function(){
				if (!this._allow_update)
					return;

				var d = new Date(this.viewDate),
					year = d.getUTCFullYear(),
					month = d.getUTCMonth();
				switch (this.viewMode){
					case 0:
						if (this.o.startDate !== -Infinity && year <= this.o.startDate.getUTCFullYear() && month <= this.o.startDate.getUTCMonth()){
							this.picker.find('.prev').css({visibility: 'hidden'});
						}
						else {
							this.picker.find('.prev').css({visibility: 'visible'});
						}
						if (this.o.endDate !== Infinity && year >= this.o.endDate.getUTCFullYear() && month >= this.o.endDate.getUTCMonth()){
							this.picker.find('.next').css({visibility: 'hidden'});
						}
						else {
							this.picker.find('.next').css({visibility: 'visible'});
						}
						break;
					case 1:
					case 2:
						if (this.o.startDate !== -Infinity && year <= this.o.startDate.getUTCFullYear()){
							this.picker.find('.prev').css({visibility: 'hidden'});
						}
						else {
							this.picker.find('.prev').css({visibility: 'visible'});
						}
						if (this.o.endDate !== Infinity && year >= this.o.endDate.getUTCFullYear()){
							this.picker.find('.next').css({visibility: 'hidden'});
						}
						else {
							this.picker.find('.next').css({visibility: 'visible'});
						}
						break;
				}
			},

			click: function(e){
				e.preventDefault();
				var target = $(e.target).closest('span, td, th'),
					year, month, day;
				if (target.length === 1){
					switch (target[0].nodeName.toLowerCase()){
						case 'th':
							switch (target[0].className){
								case 'datepicker-switch':
									this.showMode(1);
									break;
								case 'prev':
								case 'next':
									var dir = DPGlobal.modes[this.viewMode].navStep * (target[0].className === 'prev' ? -1 : 1);
									switch (this.viewMode){
										case 0:
											this.viewDate = this.moveMonth(this.viewDate, dir);
											this._trigger('changeMonth', this.viewDate);
											break;
										case 1:
										case 2:
											this.viewDate = this.moveYear(this.viewDate, dir);
											if (this.viewMode === 1)
												this._trigger('changeYear', this.viewDate);
											break;
									}
									this.fill();
									break;
								case 'today':
									var date = new Date();
									date = UTCDate(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0);

									this.showMode(-2);
									var which = this.o.todayBtn === 'linked' ? null : 'view';
									this._setDate(date, which);
									break;
								case 'clear':
									var element;
									if (this.isInput)
										element = this.element;
									else if (this.component)
										element = this.element.find('input');
									if (element)
										element.val("").change();
									this.update();
									this._trigger('changeDate');
									if (this.o.autoclose)
										this.hide();
									break;
							}
							break;
						case 'span':
							if (!target.is('.disabled')){
								this.viewDate.setUTCDate(1);
								if (target.is('.month')){
									day = 1;
									month = target.parent().find('span').index(target);
									year = this.viewDate.getUTCFullYear();
									this.viewDate.setUTCMonth(month);
									this._trigger('changeMonth', this.viewDate);
									if (this.o.minViewMode === 1){
										this._setDate(UTCDate(year, month, day));
									}
								}
								else {
									day = 1;
									month = 0;
									year = parseInt(target.text(), 10)||0;
									this.viewDate.setUTCFullYear(year);
									this._trigger('changeYear', this.viewDate);
									if (this.o.minViewMode === 2){
										this._setDate(UTCDate(year, month, day));
									}
								}
								this.showMode(-1);
								this.fill();
							}
							break;
						case 'td':
							if (target.is('.day') && !target.is('.disabled')){
								day = parseInt(target.text(), 10)||1;
								year = this.viewDate.getUTCFullYear();
								month = this.viewDate.getUTCMonth();
								if (target.is('.old')){
									if (month === 0){
										month = 11;
										year -= 1;
									}
									else {
										month -= 1;
									}
								}
								else if (target.is('.new')){
									if (month === 11){
										month = 0;
										year += 1;
									}
									else {
										month += 1;
									}
								}
								this._setDate(UTCDate(year, month, day));
							}
							break;
					}
				}
				if (this.picker.is(':visible') && this._focused_from){
					$(this._focused_from).focus();
				}
				delete this._focused_from;
			},

			_toggle_multidate: function(date){
				var ix = this.dates.contains(date);
				if (!date){
					this.dates.clear();
				}
				else if (ix !== -1){
					this.dates.remove(ix);
				}
				else {
					this.dates.push(date);
				}
				if (typeof this.o.multidate === 'number')
					while (this.dates.length > this.o.multidate)
						this.dates.remove(0);
			},

			_setDate: function(date, which){
				if (!which || which === 'date')
					this._toggle_multidate(date && new Date(date));
				if (!which || which  === 'view')
					this.viewDate = date && new Date(date);

				this.fill();
				this.setValue();
				this._trigger('changeDate');
				var element;
				if (this.isInput){
					element = this.element;
				}
				else if (this.component){
					element = this.element.find('input');
				}
				if (element){
					element.change();
				}
				if (this.o.autoclose && (!which || which === 'date')){
					this.hide();
				}
			},

			moveMonth: function(date, dir){
				if (!date)
					return undefined;
				if (!dir)
					return date;
				var new_date = new Date(date.valueOf()),
					day = new_date.getUTCDate(),
					month = new_date.getUTCMonth(),
					mag = Math.abs(dir),
					new_month, test;
				dir = dir > 0 ? 1 : -1;
				if (mag === 1){
					test = dir === -1
						// If going back one month, make sure month is not current month
						// (eg, Mar 31 -> Feb 31 == Feb 28, not Mar 02)
						? function(){
							return new_date.getUTCMonth() === month;
						}
						// If going forward one month, make sure month is as expected
						// (eg, Jan 31 -> Feb 31 == Feb 28, not Mar 02)
						: function(){
							return new_date.getUTCMonth() !== new_month;
						};
					new_month = month + dir;
					new_date.setUTCMonth(new_month);
					// Dec -> Jan (12) or Jan -> Dec (-1) -- limit expected date to 0-11
					if (new_month < 0 || new_month > 11)
						new_month = (new_month + 12) % 12;
				}
				else {
					// For magnitudes >1, move one month at a time...
					for (var i=0; i < mag; i++)
						// ...which might decrease the day (eg, Jan 31 to Feb 28, etc)...
						new_date = this.moveMonth(new_date, dir);
					// ...then reset the day, keeping it in the new month
					new_month = new_date.getUTCMonth();
					new_date.setUTCDate(day);
					test = function(){
						return new_month !== new_date.getUTCMonth();
					};
				}
				// Common date-resetting loop -- if date is beyond end of month, make it
				// end of month
				while (test()){
					new_date.setUTCDate(--day);
					new_date.setUTCMonth(new_month);
				}
				return new_date;
			},

			moveYear: function(date, dir){
				return this.moveMonth(date, dir*12);
			},

			dateWithinRange: function(date){
				return date >= this.o.startDate && date <= this.o.endDate;
			},

			keydown: function(e){
				if (this.picker.is(':not(:visible)')){
					if (e.keyCode === 27) // allow escape to hide and re-show picker
						this.show();
					return;
				}
				var dateChanged = false,
					dir, newDate, newViewDate,
					focusDate = this.focusDate || this.viewDate;
				switch (e.keyCode){
					case 27: // escape
						if (this.focusDate){
							this.focusDate = null;
							this.viewDate = this.dates.get(-1) || this.viewDate;
							this.fill();
						}
						else
							this.hide();
						e.preventDefault();
						break;
					case 37: // left
					case 39: // right
						if (!this.o.keyboardNavigation)
							break;
						dir = e.keyCode === 37 ? -1 : 1;
						if (e.ctrlKey){
							newDate = this.moveYear(this.dates.get(-1) || UTCToday(), dir);
							newViewDate = this.moveYear(focusDate, dir);
							this._trigger('changeYear', this.viewDate);
						}
						else if (e.shiftKey){
							newDate = this.moveMonth(this.dates.get(-1) || UTCToday(), dir);
							newViewDate = this.moveMonth(focusDate, dir);
							this._trigger('changeMonth', this.viewDate);
						}
						else {
							newDate = new Date(this.dates.get(-1) || UTCToday());
							newDate.setUTCDate(newDate.getUTCDate() + dir);
							newViewDate = new Date(focusDate);
							newViewDate.setUTCDate(focusDate.getUTCDate() + dir);
						}
						if (this.dateWithinRange(newDate)){
							this.focusDate = this.viewDate = newViewDate;
							this.setValue();
							this.fill();
							e.preventDefault();
						}
						break;
					case 38: // up
					case 40: // down
						if (!this.o.keyboardNavigation)
							break;
						dir = e.keyCode === 38 ? -1 : 1;
						if (e.ctrlKey){
							newDate = this.moveYear(this.dates.get(-1) || UTCToday(), dir);
							newViewDate = this.moveYear(focusDate, dir);
							this._trigger('changeYear', this.viewDate);
						}
						else if (e.shiftKey){
							newDate = this.moveMonth(this.dates.get(-1) || UTCToday(), dir);
							newViewDate = this.moveMonth(focusDate, dir);
							this._trigger('changeMonth', this.viewDate);
						}
						else {
							newDate = new Date(this.dates.get(-1) || UTCToday());
							newDate.setUTCDate(newDate.getUTCDate() + dir * 7);
							newViewDate = new Date(focusDate);
							newViewDate.setUTCDate(focusDate.getUTCDate() + dir * 7);
						}
						if (this.dateWithinRange(newDate)){
							this.focusDate = this.viewDate = newViewDate;
							this.setValue();
							this.fill();
							e.preventDefault();
						}
						break;
					case 32: // spacebar
						// Spacebar is used in manually typing dates in some formats.
						// As such, its behavior should not be hijacked.
						break;
					case 13: // enter
						focusDate = this.focusDate || this.dates.get(-1) || this.viewDate;
						this._toggle_multidate(focusDate);
						dateChanged = true;
						this.focusDate = null;
						this.viewDate = this.dates.get(-1) || this.viewDate;
						this.setValue();
						this.fill();
						if (this.picker.is(':visible')){
							e.preventDefault();
							if (this.o.autoclose)
								this.hide();
						}
						break;
					case 9: // tab
						this.focusDate = null;
						this.viewDate = this.dates.get(-1) || this.viewDate;
						this.fill();
						this.hide();
						break;
				}
				if (dateChanged){
					if (this.dates.length)
						this._trigger('changeDate');
					else
						this._trigger('clearDate');
					var element;
					if (this.isInput){
						element = this.element;
					}
					else if (this.component){
						element = this.element.find('input');
					}
					if (element){
						element.change();
					}
				}
			},

			showMode: function(dir){
				if (dir){
					this.viewMode = Math.max(this.o.minViewMode, Math.min(2, this.viewMode + dir));
				}
				this.picker
					.find('>div')
					.hide()
					.filter('.datepicker-'+DPGlobal.modes[this.viewMode].clsName)
						.css('display', 'block');
				this.updateNavArrows();
			}
		};

		var DateRangePicker = function(element, options){
			this.element = $(element);
			this.inputs = $.map(options.inputs, function(i){
				return i.jquery ? i[0] : i;
			});
			delete options.inputs;

			$(this.inputs)
				.datepicker(options)
				.bind('changeDate', $.proxy(this.dateUpdated, this));

			this.pickers = $.map(this.inputs, function(i){
				return $(i).data('datepicker');
			});
			this.updateDates();
		};
		DateRangePicker.prototype = {
			updateDates: function(){
				this.dates = $.map(this.pickers, function(i){
					return i.getUTCDate();
				});
				this.updateRanges();
			},
			updateRanges: function(){
				var range = $.map(this.dates, function(d){
					return d.valueOf();
				});
				$.each(this.pickers, function(i, p){
					p.setRange(range);
				});
			},
			dateUpdated: function(e){
				// `this.updating` is a workaround for preventing infinite recursion
				// between `changeDate` triggering and `setUTCDate` calling.  Until
				// there is a better mechanism.
				if (this.updating)
					return;
				this.updating = true;

				var dp = $(e.target).data('datepicker'),
					new_date = dp.getUTCDate(),
					i = $.inArray(e.target, this.inputs),
					l = this.inputs.length;
				if (i === -1)
					return;

				$.each(this.pickers, function(i, p){
					if (!p.getUTCDate())
						p.setUTCDate(new_date);
				});

				if (new_date < this.dates[i]){
					// Date being moved earlier/left
					while (i >= 0 && new_date < this.dates[i]){
						this.pickers[i--].setUTCDate(new_date);
					}
				}
				else if (new_date > this.dates[i]){
					// Date being moved later/right
					while (i < l && new_date > this.dates[i]){
						this.pickers[i++].setUTCDate(new_date);
					}
				}
				this.updateDates();

				delete this.updating;
			},
			remove: function(){
				$.map(this.pickers, function(p){ p.remove(); });
				delete this.element.data().datepicker;
			}
		};

		function opts_from_el(el, prefix){
			// Derive options from element data-attrs
			var data = $(el).data(),
				out = {}, inkey,
				replace = new RegExp('^' + prefix.toLowerCase() + '([A-Z])');
			prefix = new RegExp('^' + prefix.toLowerCase());
			function re_lower(_,a){
				return a.toLowerCase();
			}
			for (var key in data)
				if (prefix.test(key)){
					inkey = key.replace(replace, re_lower);
					out[inkey] = data[key];
				}
			return out;
		}

		function opts_from_locale(lang){
			// Derive options from locale plugins
			var out = {};
			// Check if "de-DE" style date is available, if not language should
			// fallback to 2 letter code eg "de"
			if (!dates[lang]){
				lang = lang.split('-')[0];
				if (!dates[lang])
					return;
			}
			var d = dates[lang];
			$.each(locale_opts, function(i,k){
				if (k in d)
					out[k] = d[k];
			});
			return out;
		}

		var old = $.fn.datepicker;
		$.fn.datepicker = function(option){
			var args = Array.apply(null, arguments);
			args.shift();
			var internal_return;
			this.each(function(){
				var $this = $(this),
					data = $this.data('datepicker'),
					options = typeof option === 'object' && option;
				if (!data){
					var elopts = opts_from_el(this, 'date'),
						// Preliminary otions
						xopts = $.extend({}, defaults, elopts, options),
						locopts = opts_from_locale(xopts.language),
						// Options priority: js args, data-attrs, locales, defaults
						opts = $.extend({}, defaults, locopts, elopts, options);
					if ($this.is('.input-daterange') || opts.inputs){
						var ropts = {
							inputs: opts.inputs || $this.find('input').toArray()
						};
						$this.data('datepicker', (data = new DateRangePicker(this, $.extend(opts, ropts))));
					}
					else {
						$this.data('datepicker', (data = new Datepicker(this, opts)));
					}
				}
				if (typeof option === 'string' && typeof data[option] === 'function'){
					internal_return = data[option].apply(data, args);
					if (internal_return !== undefined)
						return false;
				}
			});
			if (internal_return !== undefined)
				return internal_return;
			else
				return this;
		};

		var defaults = $.fn.datepicker.defaults = {
			autoclose: false,
			beforeShowDay: $.noop,
			calendarWeeks: false,
			clearBtn: false,
			daysOfWeekDisabled: [],
			endDate: Infinity,
			forceParse: true,
			format: 'mm/dd/yyyy',
			keyboardNavigation: true,
			language: 'en',
			minViewMode: 0,
			multidate: false,
			multidateSeparator: ',',
			orientation: "auto",
			rtl: false,
			startDate: -Infinity,
			startView: 0,
			todayBtn: false,
			todayHighlight: false,
			weekStart: 0
		};
		var locale_opts = $.fn.datepicker.locale_opts = [
			'format',
			'rtl',
			'weekStart'
		];
		$.fn.datepicker.Constructor = Datepicker;
		var dates = $.fn.datepicker.dates = {
			en: {
				days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
				daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
				daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
				months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
				monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
				today: "Today",
				clear: "Clear"
			}
		};

		var DPGlobal = {
			modes: [
				{
					clsName: 'days',
					navFnc: 'Month',
					navStep: 1
				},
				{
					clsName: 'months',
					navFnc: 'FullYear',
					navStep: 1
				},
				{
					clsName: 'years',
					navFnc: 'FullYear',
					navStep: 10
			}],
			isLeapYear: function(year){
				return (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0));
			},
			getDaysInMonth: function(year, month){
				return [31, (DPGlobal.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
			},
			validParts: /dd?|DD?|mm?|MM?|yy(?:yy)?/g,
			nonpunctuation: /[^ -\/:-@\[\u3400-\u9fff-`{-~\t\n\r]+/g,
			parseFormat: function(format){
				// IE treats \0 as a string end in inputs (truncating the value),
				// so it's a bad format delimiter, anyway
				var separators = format.replace(this.validParts, '\0').split('\0'),
					parts = format.match(this.validParts);
				if (!separators || !separators.length || !parts || parts.length === 0){
					throw new Error("Invalid date format.");
				}
				return {separators: separators, parts: parts};
			},
			parseDate: function(date, format, language){
				if (!date)
					return undefined;
				if (date instanceof Date)
					return date;
				if (typeof format === 'string')
					format = DPGlobal.parseFormat(format);
				var part_re = /([\-+]\d+)([dmwy])/,
					parts = date.match(/([\-+]\d+)([dmwy])/g),
					part, dir, i;
				if (/^[\-+]\d+[dmwy]([\s,]+[\-+]\d+[dmwy])*$/.test(date)){
					date = new Date();
					for (i=0; i < parts.length; i++){
						part = part_re.exec(parts[i]);
						dir = parseInt(part[1]);
						switch (part[2]){
							case 'd':
								date.setUTCDate(date.getUTCDate() + dir);
								break;
							case 'm':
								date = Datepicker.prototype.moveMonth.call(Datepicker.prototype, date, dir);
								break;
							case 'w':
								date.setUTCDate(date.getUTCDate() + dir * 7);
								break;
							case 'y':
								date = Datepicker.prototype.moveYear.call(Datepicker.prototype, date, dir);
								break;
						}
					}
					return UTCDate(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(), 0, 0, 0);
				}
				parts = date && date.match(this.nonpunctuation) || [];
				date = new Date();
				var parsed = {},
					setters_order = ['yyyy', 'yy', 'M', 'MM', 'm', 'mm', 'd', 'dd'],
					setters_map = {
						yyyy: function(d,v){
							return d.setUTCFullYear(v);
						},
						yy: function(d,v){
							return d.setUTCFullYear(2000+v);
						},
						m: function(d,v){
							if (isNaN(d))
								return d;
							v -= 1;
							while (v < 0) v += 12;
							v %= 12;
							d.setUTCMonth(v);
							while (d.getUTCMonth() !== v)
								d.setUTCDate(d.getUTCDate()-1);
							return d;
						},
						d: function(d,v){
							return d.setUTCDate(v);
						}
					},
					val, filtered;
				setters_map['M'] = setters_map['MM'] = setters_map['mm'] = setters_map['m'];
				setters_map['dd'] = setters_map['d'];
				date = UTCDate(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0);
				var fparts = format.parts.slice();
				// Remove noop parts
				if (parts.length !== fparts.length){
					fparts = $(fparts).filter(function(i,p){
						return $.inArray(p, setters_order) !== -1;
					}).toArray();
				}
				// Process remainder
				function match_part(){
					var m = this.slice(0, parts[i].length),
						p = parts[i].slice(0, m.length);
					return m === p;
				}
				if (parts.length === fparts.length){
					var cnt;
					for (i=0, cnt = fparts.length; i < cnt; i++){
						val = parseInt(parts[i], 10);
						part = fparts[i];
						if (isNaN(val)){
							switch (part){
								case 'MM':
									filtered = $(dates[language].months).filter(match_part);
									val = $.inArray(filtered[0], dates[language].months) + 1;
									break;
								case 'M':
									filtered = $(dates[language].monthsShort).filter(match_part);
									val = $.inArray(filtered[0], dates[language].monthsShort) + 1;
									break;
							}
						}
						parsed[part] = val;
					}
					var _date, s;
					for (i=0; i < setters_order.length; i++){
						s = setters_order[i];
						if (s in parsed && !isNaN(parsed[s])){
							_date = new Date(date);
							setters_map[s](_date, parsed[s]);
							if (!isNaN(_date))
								date = _date;
						}
					}
				}
				return date;
			},
			formatDate: function(date, format, language){
				if (!date)
					return '';
				if (typeof format === 'string')
					format = DPGlobal.parseFormat(format);
				var val = {
					d: date.getUTCDate(),
					D: dates[language].daysShort[date.getUTCDay()],
					DD: dates[language].days[date.getUTCDay()],
					m: date.getUTCMonth() + 1,
					M: dates[language].monthsShort[date.getUTCMonth()],
					MM: dates[language].months[date.getUTCMonth()],
					yy: date.getUTCFullYear().toString().substring(2),
					yyyy: date.getUTCFullYear()
				};
				val.dd = (val.d < 10 ? '0' : '') + val.d;
				val.mm = (val.m < 10 ? '0' : '') + val.m;
				date = [];
				var seps = $.extend([], format.separators);
				for (var i=0, cnt = format.parts.length; i <= cnt; i++){
					if (seps.length)
						date.push(seps.shift());
					date.push(val[format.parts[i]]);
				}
				return date.join('');
			},
			headTemplate: '<thead>'+
								'<tr>'+
									'<th class="prev">&laquo;</th>'+
									'<th colspan="5" class="datepicker-switch"></th>'+
									'<th class="next">&raquo;</th>'+
								'</tr>'+
							'</thead>',
			contTemplate: '<tbody><tr><td colspan="7"></td></tr></tbody>',
			footTemplate: '<tfoot>'+
								'<tr>'+
									'<th colspan="7" class="today"></th>'+
								'</tr>'+
								'<tr>'+
									'<th colspan="7" class="clear"></th>'+
								'</tr>'+
							'</tfoot>'
		};
		DPGlobal.template = '<div class="datepicker">'+
								'<div class="datepicker-days">'+
									'<table class=" table-condensed">'+
										DPGlobal.headTemplate+
										'<tbody></tbody>'+
										DPGlobal.footTemplate+
									'</table>'+
								'</div>'+
								'<div class="datepicker-months">'+
									'<table class="table-condensed">'+
										DPGlobal.headTemplate+
										DPGlobal.contTemplate+
										DPGlobal.footTemplate+
									'</table>'+
								'</div>'+
								'<div class="datepicker-years">'+
									'<table class="table-condensed">'+
										DPGlobal.headTemplate+
										DPGlobal.contTemplate+
										DPGlobal.footTemplate+
									'</table>'+
								'</div>'+
							'</div>';

		$.fn.datepicker.DPGlobal = DPGlobal;


		/* DATEPICKER NO CONFLICT
		* =================== */

		$.fn.datepicker.noConflict = function(){
			$.fn.datepicker = old;
			return this;
		};


		/* DATEPICKER DATA-API
		* ================== */

		$(document).on(
			'focus.datepicker.data-api click.datepicker.data-api',
			'[data-provide="datepicker"]',
			function(e){
				var $this = $(this);
				if ($this.data('datepicker'))
					return;
				e.preventDefault();
				// component click requires us to explicitly show it
				$this.datepicker('show');
			}
		);
		$(function(){
			$('[data-provide="datepicker-inline"]').datepicker();
		});

	}(window.jQuery));
	;
	/*!
	 * Timepicker Component for Twitter Bootstrap
	 *
	 * Copyright 2013 Joris de Wit
	 *
	 * Contributors https://github.com/jdewit/bootstrap-timepicker/graphs/contributors
	 *
	 * For the full copyright and license information, please view the LICENSE
	 * file that was distributed with this source code.
	 */
	(function($, window, document, undefined) {
	  'use strict';

	  // TIMEPICKER PUBLIC CLASS DEFINITION
	  var Timepicker = function(element, options) {
	    this.widget = '';
	    this.$element = $(element);
	    this.defaultTime = options.defaultTime;
	    this.disableFocus = options.disableFocus;
	    this.disableMousewheel = options.disableMousewheel;
	    this.isOpen = options.isOpen;
	    this.minuteStep = options.minuteStep;
	    this.modalBackdrop = options.modalBackdrop;
	    this.orientation = options.orientation;
	    this.secondStep = options.secondStep;
	    this.showInputs = options.showInputs;
	    this.showMeridian = options.showMeridian;
	    this.showSeconds = options.showSeconds;
	    this.template = options.template;
	    this.appendWidgetTo = options.appendWidgetTo;
	    this.showWidgetOnAddonClick = options.showWidgetOnAddonClick;

	    this._init();
	  };

	  Timepicker.prototype = {

	    constructor: Timepicker,
	    _init: function() {
	      var self = this;

	      if (this.showWidgetOnAddonClick && (this.$element.parent().hasClass('input-append') || this.$element.parent().hasClass('input-prepend'))) {
	        this.$element.parent('.input-append, .input-prepend').find('.add-on').on({
	          'click.timepicker': $.proxy(this.showWidget, this)
	        });
	        this.$element.on({
	          'focus.timepicker': $.proxy(this.highlightUnit, this),
	          'click.timepicker': $.proxy(this.highlightUnit, this),
	          'keydown.timepicker': $.proxy(this.elementKeydown, this),
	          'blur.timepicker': $.proxy(this.blurElement, this),
	          'mousewheel.timepicker DOMMouseScroll.timepicker': $.proxy(this.mousewheel, this)
	        });
	      } else {
	        if (this.template) {
	          this.$element.on({
	            'focus.timepicker': $.proxy(this.showWidget, this),
	            'click.timepicker': $.proxy(this.showWidget, this),
	            'blur.timepicker': $.proxy(this.blurElement, this),
	            'mousewheel.timepicker DOMMouseScroll.timepicker': $.proxy(this.mousewheel, this)
	          });
	        } else {
	          this.$element.on({
	            'focus.timepicker': $.proxy(this.highlightUnit, this),
	            'click.timepicker': $.proxy(this.highlightUnit, this),
	            'keydown.timepicker': $.proxy(this.elementKeydown, this),
	            'blur.timepicker': $.proxy(this.blurElement, this),
	            'mousewheel.timepicker DOMMouseScroll.timepicker': $.proxy(this.mousewheel, this)
	          });
	        }
	      }

	      if (this.template !== false) {
	        this.$widget = $(this.getTemplate()).on('click', $.proxy(this.widgetClick, this));
	      } else {
	        this.$widget = false;
	      }

	      if (this.showInputs && this.$widget !== false) {
	        this.$widget.find('input').each(function() {
	          $(this).on({
	            'click.timepicker': function() { $(this).select(); },
	            'keydown.timepicker': $.proxy(self.widgetKeydown, self),
	            'keyup.timepicker': $.proxy(self.widgetKeyup, self)
	          });
	        });
	      }

	      this.setDefaultTime(this.defaultTime);
	    },

	    blurElement: function() {
	      this.highlightedUnit = null;
	      this.updateFromElementVal();
	    },

	    clear: function() {
	      this.hour = '';
	      this.minute = '';
	      this.second = '';
	      this.meridian = '';

	      this.$element.val('');
	    },

	    decrementHour: function() {
	      if (this.showMeridian) {
	        if (this.hour === 1) {
	          this.hour = 12;
	        } else if (this.hour === 12) {
	          this.hour--;

	          return this.toggleMeridian();
	        } else if (this.hour === 0) {
	          this.hour = 11;

	          return this.toggleMeridian();
	        } else {
	          this.hour--;
	        }
	      } else {
	        if (this.hour <= 0) {
	          this.hour = 23;
	        } else {
	          this.hour--;
	        }
	      }
	    },

	    decrementMinute: function(step) {
	      var newVal;

	      if (step) {
	        newVal = this.minute - step;
	      } else {
	        newVal = this.minute - this.minuteStep;
	      }

	      if (newVal < 0) {
	        this.decrementHour();
	        this.minute = newVal + 60;
	      } else {
	        this.minute = newVal;
	      }
	    },

	    decrementSecond: function() {
	      var newVal = this.second - this.secondStep;

	      if (newVal < 0) {
	        this.decrementMinute(true);
	        this.second = newVal + 60;
	      } else {
	        this.second = newVal;
	      }
	    },

	    elementKeydown: function(e) {
	      switch (e.keyCode) {
	      case 9: //tab
	      case 27: // escape
	        this.updateFromElementVal();
	        break;
	      case 37: // left arrow
	        e.preventDefault();
	        this.highlightPrevUnit();
	        break;
	      case 38: // up arrow
	        e.preventDefault();
	        switch (this.highlightedUnit) {
	        case 'hour':
	          this.incrementHour();
	          this.highlightHour();
	          break;
	        case 'minute':
	          this.incrementMinute();
	          this.highlightMinute();
	          break;
	        case 'second':
	          this.incrementSecond();
	          this.highlightSecond();
	          break;
	        case 'meridian':
	          this.toggleMeridian();
	          this.highlightMeridian();
	          break;
	        }
	        this.update();
	        break;
	      case 39: // right arrow
	        e.preventDefault();
	        this.highlightNextUnit();
	        break;
	      case 40: // down arrow
	        e.preventDefault();
	        switch (this.highlightedUnit) {
	        case 'hour':
	          this.decrementHour();
	          this.highlightHour();
	          break;
	        case 'minute':
	          this.decrementMinute();
	          this.highlightMinute();
	          break;
	        case 'second':
	          this.decrementSecond();
	          this.highlightSecond();
	          break;
	        case 'meridian':
	          this.toggleMeridian();
	          this.highlightMeridian();
	          break;
	        }

	        this.update();
	        break;
	      }
	    },

	    getCursorPosition: function() {
	      var input = this.$element.get(0);

	      if ('selectionStart' in input) {// Standard-compliant browsers

	        return input.selectionStart;
	      } else if (document.selection) {// IE fix
	        input.focus();
	        var sel = document.selection.createRange(),
	          selLen = document.selection.createRange().text.length;

	        sel.moveStart('character', - input.value.length);

	        return sel.text.length - selLen;
	      }
	    },

	    getTemplate: function() {
	      var template,
	        hourTemplate,
	        minuteTemplate,
	        secondTemplate,
	        meridianTemplate,
	        templateContent;

	      if (this.showInputs) {
	        hourTemplate = '<input type="text" class="bootstrap-timepicker-hour" maxlength="2"/>';
	        minuteTemplate = '<input type="text" class="bootstrap-timepicker-minute" maxlength="2"/>';
	        secondTemplate = '<input type="text" class="bootstrap-timepicker-second" maxlength="2"/>';
	        meridianTemplate = '<input type="text" class="bootstrap-timepicker-meridian" maxlength="2"/>';
	      } else {
	        hourTemplate = '<span class="bootstrap-timepicker-hour"></span>';
	        minuteTemplate = '<span class="bootstrap-timepicker-minute"></span>';
	        secondTemplate = '<span class="bootstrap-timepicker-second"></span>';
	        meridianTemplate = '<span class="bootstrap-timepicker-meridian"></span>';
	      }

	      templateContent = '<table>'+
	         '<tr>'+
	           '<td><a href="#" data-action="incrementHour"><i class="icon-chevron-up"></i></a></td>'+
	           '<td class="separator">&nbsp;</td>'+
	           '<td><a href="#" data-action="incrementMinute"><i class="icon-chevron-up"></i></a></td>'+
	           (this.showSeconds ?
	             '<td class="separator">&nbsp;</td>'+
	             '<td><a href="#" data-action="incrementSecond"><i class="icon-chevron-up"></i></a></td>'
	           : '') +
	           (this.showMeridian ?
	             '<td class="separator">&nbsp;</td>'+
	             '<td class="meridian-column"><a href="#" data-action="toggleMeridian"><i class="icon-chevron-up"></i></a></td>'
	           : '') +
	         '</tr>'+
	         '<tr>'+
	           '<td>'+ hourTemplate +'</td> '+
	           '<td class="separator">:</td>'+
	           '<td>'+ minuteTemplate +'</td> '+
	           (this.showSeconds ?
	            '<td class="separator">:</td>'+
	            '<td>'+ secondTemplate +'</td>'
	           : '') +
	           (this.showMeridian ?
	            '<td class="separator">&nbsp;</td>'+
	            '<td>'+ meridianTemplate +'</td>'
	           : '') +
	         '</tr>'+
	         '<tr>'+
	           '<td><a href="#" data-action="decrementHour"><i class="icon-chevron-down"></i></a></td>'+
	           '<td class="separator"></td>'+
	           '<td><a href="#" data-action="decrementMinute"><i class="icon-chevron-down"></i></a></td>'+
	           (this.showSeconds ?
	            '<td class="separator">&nbsp;</td>'+
	            '<td><a href="#" data-action="decrementSecond"><i class="icon-chevron-down"></i></a></td>'
	           : '') +
	           (this.showMeridian ?
	            '<td class="separator">&nbsp;</td>'+
	            '<td><a href="#" data-action="toggleMeridian"><i class="icon-chevron-down"></i></a></td>'
	           : '') +
	         '</tr>'+
	       '</table>';

	      switch(this.template) {
	      case 'modal':
	        template = '<div class="bootstrap-timepicker-widget modal hide fade in" data-backdrop="'+ (this.modalBackdrop ? 'true' : 'false') +'">'+
	          '<div class="modal-header">'+
	            '<a href="#" class="close" data-dismiss="modal">??/a>'+
	            '<h3>Pick a Time</h3>'+
	          '</div>'+
	          '<div class="modal-content">'+
	            templateContent +
	          '</div>'+
	          '<div class="modal-footer">'+
	            '<a href="#" class="btn btn-primary" data-dismiss="modal">OK</a>'+
	          '</div>'+
	        '</div>';
	        break;
	      case 'dropdown':
	        template = '<div class="bootstrap-timepicker-widget dropdown-menu">'+ templateContent +'</div>';
	        break;
	      }

	      return template;
	    },

	    getTime: function() {
	      if (this.hour === '') {
	        return '';
	      }

	      return this.hour + ':' + (this.minute.toString().length === 1 ? '0' + this.minute : this.minute) + (this.showSeconds ? ':' + (this.second.toString().length === 1 ? '0' + this.second : this.second) : '') + (this.showMeridian ? ' ' + this.meridian : '');
	    },

	    hideWidget: function() {
	      if (this.isOpen === false) {
	        return;
	      }

	      this.$element.trigger({
	        'type': 'hide.timepicker',
	        'time': {
	          'value': this.getTime(),
	          'hours': this.hour,
	          'minutes': this.minute,
	          'seconds': this.second,
	          'meridian': this.meridian
	        }
	      });

	      if (this.template === 'modal' && this.$widget.modal) {
	        this.$widget.modal('hide');
	      } else {
	        this.$widget.removeClass('open');
	      }

	      $(document).off('mousedown.timepicker, touchend.timepicker');

	      this.isOpen = false;
	      // show/hide approach taken by datepicker
	      this.$widget.detach();
	    },

	    highlightUnit: function() {
	      this.position = this.getCursorPosition();
	      if (this.position >= 0 && this.position <= 2) {
	        this.highlightHour();
	      } else if (this.position >= 3 && this.position <= 5) {
	        this.highlightMinute();
	      } else if (this.position >= 6 && this.position <= 8) {
	        if (this.showSeconds) {
	          this.highlightSecond();
	        } else {
	          this.highlightMeridian();
	        }
	      } else if (this.position >= 9 && this.position <= 11) {
	        this.highlightMeridian();
	      }
	    },

	    highlightNextUnit: function() {
	      switch (this.highlightedUnit) {
	      case 'hour':
	        this.highlightMinute();
	        break;
	      case 'minute':
	        if (this.showSeconds) {
	          this.highlightSecond();
	        } else if (this.showMeridian){
	          this.highlightMeridian();
	        } else {
	          this.highlightHour();
	        }
	        break;
	      case 'second':
	        if (this.showMeridian) {
	          this.highlightMeridian();
	        } else {
	          this.highlightHour();
	        }
	        break;
	      case 'meridian':
	        this.highlightHour();
	        break;
	      }
	    },

	    highlightPrevUnit: function() {
	      switch (this.highlightedUnit) {
	      case 'hour':
	        if(this.showMeridian){
	          this.highlightMeridian();
	        } else if (this.showSeconds) {
	          this.highlightSecond();
	        } else {
	          this.highlightMinute();
	        }
	        break;
	      case 'minute':
	        this.highlightHour();
	        break;
	      case 'second':
	        this.highlightMinute();
	        break;
	      case 'meridian':
	        if (this.showSeconds) {
	          this.highlightSecond();
	        } else {
	          this.highlightMinute();
	        }
	        break;
	      }
	    },

	    highlightHour: function() {
	      var $element = this.$element.get(0),
	          self = this;

	      this.highlightedUnit = 'hour';

				if ($element.setSelectionRange) {
					setTimeout(function() {
	          if (self.hour < 10) {
	            $element.setSelectionRange(0,1);
	          } else {
	            $element.setSelectionRange(0,2);
	          }
					}, 0);
				}
	    },

	    highlightMinute: function() {
	      var $element = this.$element.get(0),
	          self = this;

	      this.highlightedUnit = 'minute';

				if ($element.setSelectionRange) {
					setTimeout(function() {
	          if (self.hour < 10) {
	            $element.setSelectionRange(2,4);
	          } else {
	            $element.setSelectionRange(3,5);
	          }
					}, 0);
				}
	    },

	    highlightSecond: function() {
	      var $element = this.$element.get(0),
	          self = this;

	      this.highlightedUnit = 'second';

				if ($element.setSelectionRange) {
					setTimeout(function() {
	          if (self.hour < 10) {
	            $element.setSelectionRange(5,7);
	          } else {
	            $element.setSelectionRange(6,8);
	          }
					}, 0);
				}
	    },

	    highlightMeridian: function() {
	      var $element = this.$element.get(0),
	          self = this;

	      this.highlightedUnit = 'meridian';

				if ($element.setSelectionRange) {
					if (this.showSeconds) {
						setTimeout(function() {
	            if (self.hour < 10) {
	              $element.setSelectionRange(8,10);
	            } else {
	              $element.setSelectionRange(9,11);
	            }
						}, 0);
					} else {
						setTimeout(function() {
	            if (self.hour < 10) {
	              $element.setSelectionRange(5,7);
	            } else {
	              $element.setSelectionRange(6,8);
	            }
						}, 0);
					}
				}
	    },

	    incrementHour: function() {
	      if (this.showMeridian) {
	        if (this.hour === 11) {
	          this.hour++;
	          return this.toggleMeridian();
	        } else if (this.hour === 12) {
	          this.hour = 0;
	        }
	      }
	      if (this.hour === 23) {
	        this.hour = 0;

	        return;
	      }
	      this.hour++;
	    },

	    incrementMinute: function(step) {
	      var newVal;

	      if (step) {
	        newVal = this.minute + step;
	      } else {
	        newVal = this.minute + this.minuteStep - (this.minute % this.minuteStep);
	      }

	      if (newVal > 59) {
	        this.incrementHour();
	        this.minute = newVal - 60;
	      } else {
	        this.minute = newVal;
	      }
	    },

	    incrementSecond: function() {
	      var newVal = this.second + this.secondStep - (this.second % this.secondStep);

	      if (newVal > 59) {
	        this.incrementMinute(true);
	        this.second = newVal - 60;
	      } else {
	        this.second = newVal;
	      }
	    },

	    mousewheel: function(e) {
	      if (this.disableMousewheel) {
	        return;
	      }

	      e.preventDefault();
	      e.stopPropagation();

	      var delta = e.originalEvent.wheelDelta || -e.originalEvent.detail,
	          scrollTo = null;

	      if (e.type === 'mousewheel') {
	        scrollTo = (e.originalEvent.wheelDelta * -1);
	      }
	      else if (e.type === 'DOMMouseScroll') {
	        scrollTo = 40 * e.originalEvent.detail;
	      }

	      if (scrollTo) {
	        e.preventDefault();
	        $(this).scrollTop(scrollTo + $(this).scrollTop());
	      }

	      switch (this.highlightedUnit) {
	      case 'minute':
	        if (delta > 0) {
	          this.incrementMinute();
	        } else {
	          this.decrementMinute();
	        }
	        this.highlightMinute();
	        break;
	      case 'second':
	        if (delta > 0) {
	          this.incrementSecond();
	        } else {
	          this.decrementSecond();
	        }
	        this.highlightSecond();
	        break;
	      case 'meridian':
	        this.toggleMeridian();
	        this.highlightMeridian();
	        break;
	      default:
	        if (delta > 0) {
	          this.incrementHour();
	        } else {
	          this.decrementHour();
	        }
	        this.highlightHour();
	        break;
	      }

	      return false;
	    },

	    // This method was adapted from bootstrap-datepicker.
	    place : function() {
	      if (this.isInline) {
	        return;
	      }
	      var widgetWidth = this.$widget.outerWidth(), widgetHeight = this.$widget.outerHeight(), visualPadding = 10, windowWidth =
	        $(window).width(), windowHeight = $(window).height(), scrollTop = $(window).scrollTop();

	      var zIndex = parseInt(this.$element.parents().filter(function() {}).first().css('z-index'), 10) + 10;
	      var offset = this.component ? this.component.parent().offset() : this.$element.offset();
	      var height = this.component ? this.component.outerHeight(true) : this.$element.outerHeight(false);
	      var width = this.component ? this.component.outerWidth(true) : this.$element.outerWidth(false);
	      var left = offset.left, top = offset.top;

	      this.$widget.removeClass('timepicker-orient-top timepicker-orient-bottom timepicker-orient-right timepicker-orient-left');

	      if (this.orientation.x !== 'auto') {
	        this.$widget.addClass('timepicker-orient-' + this.orientation.x);
	        if (this.orientation.x === 'right') {
	          left -= widgetWidth - width;
	        }
	      } else{
	        // auto x orientation is best-placement: if it crosses a window edge, fudge it sideways
	        // Default to left
	        this.$widget.addClass('timepicker-orient-left');
	        if (offset.left < 0) {
	          left -= offset.left - visualPadding;
	        } else if (offset.left + widgetWidth > windowWidth) {
	          left = windowWidth - widgetWidth - visualPadding;
	        }
	      }
	      // auto y orientation is best-situation: top or bottom, no fudging, decision based on which shows more of the widget
	      var yorient = this.orientation.y, topOverflow, bottomOverflow;
	      if (yorient === 'auto') {
	        topOverflow = -scrollTop + offset.top - widgetHeight;
	        bottomOverflow = scrollTop + windowHeight - (offset.top + height + widgetHeight);
	        if (Math.max(topOverflow, bottomOverflow) === bottomOverflow) {
	          yorient = 'top';
	        } else {
	          yorient = 'bottom';
	        }
	      }
	      this.$widget.addClass('timepicker-orient-' + yorient);
	      if (yorient === 'top'){
	        top += height;
	      } else{
	        top -= widgetHeight + parseInt(this.$widget.css('padding-top'), 10);
	      }

	      this.$widget.css({
	        top : top,
	        left : left,
	        zIndex : zIndex
	      });
	    },

	    remove: function() {
	      $('document').off('.timepicker');
	      if (this.$widget) {
	        this.$widget.remove();
	      }
	      delete this.$element.data().timepicker;
	    },

	    setDefaultTime: function(defaultTime) {
	      if (!this.$element.val()) {
	        if (defaultTime === 'current') {
	          var dTime = new Date(),
	            hours = dTime.getHours(),
	            minutes = dTime.getMinutes(),
	            seconds = dTime.getSeconds(),
	            meridian = 'AM';

	          if (seconds !== 0) {
	            seconds = Math.ceil(dTime.getSeconds() / this.secondStep) * this.secondStep;
	            if (seconds === 60) {
	              minutes += 1;
	              seconds = 0;
	            }
	          }

	          if (minutes !== 0) {
	            minutes = Math.ceil(dTime.getMinutes() / this.minuteStep) * this.minuteStep;
	            if (minutes === 60) {
	              hours += 1;
	              minutes = 0;
	            }
	          }

	          if (this.showMeridian) {
	            if (hours === 0) {
	              hours = 12;
	            } else if (hours >= 12) {
	              if (hours > 12) {
	                hours = hours - 12;
	              }
	              meridian = 'PM';
	            } else {
	              meridian = 'AM';
	            }
	          }

	          this.hour = hours;
	          this.minute = minutes;
	          this.second = seconds;
	          this.meridian = meridian;

	          this.update();

	        } else if (defaultTime === false) {
	          this.hour = 0;
	          this.minute = 0;
	          this.second = 0;
	          this.meridian = 'AM';
	        } else {
	          this.setTime(defaultTime);
	        }
	      } else {
	        this.updateFromElementVal();
	      }
	    },

	    setTime: function(time, ignoreWidget) {
	      if (!time) {
	        this.clear();
	        return;
	      }

	      var timeArray,
	          hour,
	          minute,
	          second,
	          meridian;

	      if (typeof time === 'object' && time.getMonth){
	        // this is a date object
	        hour    = time.getHours();
	        minute  = time.getMinutes();
	        second  = time.getSeconds();

	        if (this.showMeridian){
	          meridian = 'AM';
	          if (hour > 12){
	            meridian = 'PM';
	            hour = hour % 12;
	          }

	          if (hour === 12){
	            meridian = 'PM';
	          }
	        }
	      } else {
	        if (time.match(/p/i) !== null) {
	          meridian = 'PM';
	        } else {
	          meridian = 'AM';
	        }

	        time = time.replace(/[^0-9\:]/g, '');

	        timeArray = time.split(':');

	        hour = timeArray[0] ? timeArray[0].toString() : timeArray.toString();
	        minute = timeArray[1] ? timeArray[1].toString() : '';
	        second = timeArray[2] ? timeArray[2].toString() : '';

	        // idiot proofing
	        if (hour.length > 4) {
	          second = hour.substr(4, 2);
	        }
	        if (hour.length > 2) {
	          minute = hour.substr(2, 2);
	          hour = hour.substr(0, 2);
	        }
	        if (minute.length > 2) {
	          second = minute.substr(2, 2);
	          minute = minute.substr(0, 2);
	        }
	        if (second.length > 2) {
	          second = second.substr(2, 2);
	        }

	        hour = parseInt(hour, 10);
	        minute = parseInt(minute, 10);
	        second = parseInt(second, 10);

	        if (isNaN(hour)) {
	          hour = 0;
	        }
	        if (isNaN(minute)) {
	          minute = 0;
	        }
	        if (isNaN(second)) {
	          second = 0;
	        }

	        if (this.showMeridian) {
	          if (hour < 1) {
	            hour = 1;
	          } else if (hour > 12) {
	            hour = 12;
	          }
	        } else {
	          if (hour >= 24) {
	            hour = 23;
	          } else if (hour < 0) {
	            hour = 0;
	          }
	          if (hour < 13 && meridian === 'PM') {
	            hour = hour + 12;
	          }
	        }

	        if (minute < 0) {
	          minute = 0;
	        } else if (minute >= 60) {
	          minute = 59;
	        }

	        if (this.showSeconds) {
	          if (isNaN(second)) {
	            second = 0;
	          } else if (second < 0) {
	            second = 0;
	          } else if (second >= 60) {
	            second = 59;
	          }
	        }
	      }

	      this.hour = hour;
	      this.minute = minute;
	      this.second = second;
	      this.meridian = meridian;

	      this.update(ignoreWidget);
	    },

	    showWidget: function() {
	      if (this.isOpen) {
	        return;
	      }

	      if (this.$element.is(':disabled')) {
	        return;
	      }

	      // show/hide approach taken by datepicker
	      this.$widget.appendTo(this.appendWidgetTo);
	      var self = this;
	      $(document).on('mousedown.timepicker, touchend.timepicker', function (e) {
	        // This condition was inspired by bootstrap-datepicker.
	        // The element the timepicker is invoked on is the input but it has a sibling for addon/button.
	        if (!(self.$element.parent().find(e.target).length ||
	            self.$widget.is(e.target) ||
	            self.$widget.find(e.target).length)) {
	          self.hideWidget();
	        }
	      });

	      this.$element.trigger({
	        'type': 'show.timepicker',
	        'time': {
	          'value': this.getTime(),
	          'hours': this.hour,
	          'minutes': this.minute,
	          'seconds': this.second,
	          'meridian': this.meridian
	        }
	      });

	      if (this.template !== 'modal') this.place();
	      if (this.disableFocus) {
	        this.$element.blur();
	      }

	      // widget shouldn't be empty on open
	      if (this.hour === '') {
	        if (this.defaultTime) {
	          this.setDefaultTime(this.defaultTime);
	        } else {
	          this.setTime('0:0:0');
	        }
	      }

	      if (this.template === 'modal' && this.$widget.modal) {
	        this.$widget.modal('show').on('hidden', $.proxy(this.hideWidget, this));
	      } else {
	        if (this.isOpen === false) {
	          this.$widget.addClass('open');
	        }
	      }

	      this.isOpen = true;
	    },

	    toggleMeridian: function() {
	      this.meridian = this.meridian === 'AM' ? 'PM' : 'AM';
	    },

	    update: function(ignoreWidget) {
	      this.updateElement();
	      if (!ignoreWidget) {
	        this.updateWidget();
	      }

	      this.$element.trigger({
	        'type': 'changeTime.timepicker',
	        'time': {
	          'value': this.getTime(),
	          'hours': this.hour,
	          'minutes': this.minute,
	          'seconds': this.second,
	          'meridian': this.meridian
	        }
	      });
	    },

	    updateElement: function() {
	      this.$element.val(this.getTime()).change();
	    },

	    updateFromElementVal: function() {
	      this.setTime(this.$element.val());
	    },

	    updateWidget: function() {
	      if (this.$widget === false) {
	        return;
	      }

	      var hour = this.hour,
	          minute = this.minute.toString().length === 1 ? '0' + this.minute : this.minute,
	          second = this.second.toString().length === 1 ? '0' + this.second : this.second;

	      if (this.showInputs) {
	        this.$widget.find('input.bootstrap-timepicker-hour').val(hour);
	        this.$widget.find('input.bootstrap-timepicker-minute').val(minute);

	        if (this.showSeconds) {
	          this.$widget.find('input.bootstrap-timepicker-second').val(second);
	        }
	        if (this.showMeridian) {
	          this.$widget.find('input.bootstrap-timepicker-meridian').val(this.meridian);
	        }
	      } else {
	        this.$widget.find('span.bootstrap-timepicker-hour').text(hour);
	        this.$widget.find('span.bootstrap-timepicker-minute').text(minute);

	        if (this.showSeconds) {
	          this.$widget.find('span.bootstrap-timepicker-second').text(second);
	        }
	        if (this.showMeridian) {
	          this.$widget.find('span.bootstrap-timepicker-meridian').text(this.meridian);
	        }
	      }
	    },

	    updateFromWidgetInputs: function() {
	      if (this.$widget === false) {
	        return;
	      }

	      var t = this.$widget.find('input.bootstrap-timepicker-hour').val() + ':' +
	              this.$widget.find('input.bootstrap-timepicker-minute').val() +
	              (this.showSeconds ? ':' + this.$widget.find('input.bootstrap-timepicker-second').val() : '') +
	              (this.showMeridian ? this.$widget.find('input.bootstrap-timepicker-meridian').val() : '')
	      ;

	      this.setTime(t, true);
	    },

	    widgetClick: function(e) {
	      e.stopPropagation();
	      e.preventDefault();

	      var $input = $(e.target),
	          action = $input.closest('a').data('action');

	      if (action) {
	        this[action]();
	      }
	      this.update();

	      if ($input.is('input')) {
	        $input.get(0).setSelectionRange(0,2);
	      }
	    },

	    widgetKeydown: function(e) {
	      var $input = $(e.target),
	          name = $input.attr('class').replace('bootstrap-timepicker-', '');

	      switch (e.keyCode) {
	      case 9: //tab
	        if ((this.showMeridian && name === 'meridian') || (this.showSeconds && name === 'second') || (!this.showMeridian && !this.showSeconds && name === 'minute')) {
	          return this.hideWidget();
	        }
	        break;
	      case 27: // escape
	        this.hideWidget();
	        break;
	      case 38: // up arrow
	        e.preventDefault();
	        switch (name) {
	        case 'hour':
	          this.incrementHour();
	          break;
	        case 'minute':
	          this.incrementMinute();
	          break;
	        case 'second':
	          this.incrementSecond();
	          break;
	        case 'meridian':
	          this.toggleMeridian();
	          break;
	        }
	        this.setTime(this.getTime());
	        $input.get(0).setSelectionRange(0,2);
	        break;
	      case 40: // down arrow
	        e.preventDefault();
	        switch (name) {
	        case 'hour':
	          this.decrementHour();
	          break;
	        case 'minute':
	          this.decrementMinute();
	          break;
	        case 'second':
	          this.decrementSecond();
	          break;
	        case 'meridian':
	          this.toggleMeridian();
	          break;
	        }
	        this.setTime(this.getTime());
	        $input.get(0).setSelectionRange(0,2);
	        break;
	      }
	    },

	    widgetKeyup: function(e) {
	      if ((e.keyCode === 65) || (e.keyCode === 77) || (e.keyCode === 80) || (e.keyCode === 46) || (e.keyCode === 8) || (e.keyCode >= 46 && e.keyCode <= 57)) {
	        this.updateFromWidgetInputs();
	      }
	    }
	  };

	  //TIMEPICKER PLUGIN DEFINITION
	  $.fn.timepicker = function(option) {
	    var args = Array.apply(null, arguments);
	    args.shift();
	    return this.each(function() {
	      var $this = $(this),
	        data = $this.data('timepicker'),
	        options = typeof option === 'object' && option;

	      if (!data) {
	        $this.data('timepicker', (data = new Timepicker(this, $.extend({}, $.fn.timepicker.defaults, options, $(this).data()))));
	      }

	      if (typeof option === 'string') {
	        data[option].apply(data, args);
	      }
	    });
	  };

	  $.fn.timepicker.defaults = {
	    defaultTime: 'current',
	    disableFocus: false,
	    disableMousewheel: false,
	    isOpen: false,
	    minuteStep: 15,
	    modalBackdrop: false,
	    orientation: { x: 'auto', y: 'auto'},
	    secondStep: 15,
	    showSeconds: false,
	    showInputs: true,
	    showMeridian: true,
	    template: 'dropdown',
	    appendWidgetTo: 'body',
	    showWidgetOnAddonClick: true
	  };

	  $.fn.timepicker.Constructor = Timepicker;

	})(jQuery, window, document);
	;
	/* =========================================================
	 * bootstrap-tabdrop.js 
	 * http://www.eyecon.ro/bootstrap-tabdrop
	 * =========================================================
	 * Copyright 2012 Stefan Petre
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 * ========================================================= */
	 
	!function( $ ) {

		var WinReszier = (function(){
			var registered = [];
			var inited = false;
			var timer;
			var resize = function(ev) {
				clearTimeout(timer);
				timer = setTimeout(notify, 100);
			};
			var notify = function() {
				for(var i=0, cnt=registered.length; i<cnt; i++) {
					registered[i].apply();
				}
			};
			return {
				register: function(fn) {
					registered.push(fn);
					if (inited === false) {
						$(window).bind('resize', resize);
						inited = true;
					}
				},
				unregister: function(fn) {
					for(var i=0, cnt=registered.length; i<cnt; i++) {
						if (registered[i] == fn) {
							delete registered[i];
							break;
						}
					}
				}
			}
		}());

		var TabDrop = function(element, options) {
			this.element = $(element);
			this.dropdown = $('<li class="dropdown hide pull-right tabdrop"><a class="dropdown-toggle" data-toggle="dropdown" href="#">'+options.text+' <b class="caret"></b></a><ul class="dropdown-menu"></ul></li>')
								.prependTo(this.element);
			if (this.element.parent().is('.tabs-below')) {
				this.dropdown.addClass('dropup');
			}
			WinReszier.register($.proxy(this.layout, this));
			this.layout();
		};

		TabDrop.prototype = {
			constructor: TabDrop,

			layout: function() {
				var collection = [];
				this.dropdown.removeClass('hide');
				this.element
					.append(this.dropdown.find('li'))
					.find('>li')
					.not('.tabdrop')
					.each(function(){
						if(this.offsetTop > 0) {
							collection.push(this);
						}
					});
				if (collection.length > 0) {
					collection = $(collection);
					this.dropdown
						.find('ul')
						.empty()
						.append(collection);
					if (this.dropdown.find('.active').length == 1) {
						this.dropdown.addClass('active');
					} else {
						this.dropdown.removeClass('active');
					}
				} else {
					this.dropdown.addClass('hide');
				}
			}
		}

		$.fn.tabdrop = function ( option ) {
			return this.each(function () {
				var $this = $(this),
					data = $this.data('tabdrop'),
					options = typeof option === 'object' && option;
				if (!data)  {
					$this.data('tabdrop', (data = new TabDrop(this, $.extend({}, $.fn.tabdrop.defaults,options))));
				}
				if (typeof option == 'string') {
					data[option]();
				}
			})
		};

		$.fn.tabdrop.defaults = {
			text: '<i class="icon-align-justify"></i>'
		};

		$.fn.tabdrop.Constructor = TabDrop;

	}( window.jQuery );;
	/*
	 * jQuery MiniColors: A tiny color picker built on jQuery
	 *
	 * Copyright Cory LaViska for A Beautiful Site, LLC. (http://www.abeautifulsite.net/)
	 *
	 * Licensed under the MIT license: http://opensource.org/licenses/MIT
	 *
	 */
	if(jQuery) (function($) {
		
		// Defaults
		$.minicolors = {
			defaults: {
				animationSpeed: 50,
				animationEasing: 'swing',
				change: null,
				changeDelay: 0,
				control: 'hue',
				defaultValue: '',
				hide: null,
				hideSpeed: 100,
				inline: false,
				letterCase: 'lowercase',
				opacity: false,
				position: 'bottom left',
				show: null,
				showSpeed: 100,
				theme: 'default'
			}
		};
		
		// Public methods
		$.extend($.fn, {
			minicolors: function(method, data) {
				
				switch(method) {
					
					// Destroy the control
					case 'destroy':
						$(this).each( function() {
							destroy($(this));
						});
						return $(this);
					
					// Hide the color picker
					case 'hide':
						hide();
						return $(this);
					
					// Get/set opacity
					case 'opacity':
						// Getter
						if( data === undefined ) {
							// Getter
							return $(this).attr('data-opacity');
						} else {
							// Setter
							$(this).each( function() {
								updateFromInput($(this).attr('data-opacity', data));
							});
						}
						return $(this);
					
					// Get an RGB(A) object based on the current color/opacity
					case 'rgbObject':
						return rgbObject($(this), method === 'rgbaObject');
					
					// Get an RGB(A) string based on the current color/opacity
					case 'rgbString':
					case 'rgbaString':
						return rgbString($(this), method === 'rgbaString');
					
					// Get/set settings on the fly
					case 'settings':
						if( data === undefined ) {
							return $(this).data('minicolors-settings');
						} else {
							// Setter
							$(this).each( function() {
								var settings = $(this).data('minicolors-settings') || {};
								destroy($(this));
								$(this).minicolors($.extend(true, settings, data));
							});
						}
						return $(this);
					
					// Show the color picker
					case 'show':
						show( $(this).eq(0) );
						return $(this);
					
					// Get/set the hex color value
					case 'value':
						if( data === undefined ) {
							// Getter
							return $(this).val();
						} else {
							// Setter
							$(this).each( function() {
								updateFromInput($(this).val(data));
							});
						}
						return $(this);
					
					// Initializes the control
					default:
						if( method !== 'create' ) data = method;
						$(this).each( function() {
							init($(this), data);
						});
						return $(this);
					
				}
				
			}
		});
		
		// Initialize input elements
		function init(input, settings) {
			
			var minicolors = $('<div class="minicolors" />'),
				defaults = $.minicolors.defaults;
			
			// Do nothing if already initialized
			if( input.data('minicolors-initialized') ) return;
			
			// Handle settings
			settings = $.extend(true, {}, defaults, settings);
			
			// The wrapper
			minicolors
				.addClass('minicolors-theme-' + settings.theme)
				.toggleClass('minicolors-with-opacity', settings.opacity);
			
			// Custom positioning
			if( settings.position !== undefined ) {
				$.each(settings.position.split(' '), function() {
					minicolors.addClass('minicolors-position-' + this);
				});
			}
			
			// The input
			input
				.addClass('minicolors-input')
				.data('minicolors-initialized', false)
				.data('minicolors-settings', settings)
				.prop('size', 7)
				.wrap(minicolors)
				.after(
					'<div class="minicolors-panel minicolors-slider-' + settings.control + '">' + 
						'<div class="minicolors-slider">' + 
							'<div class="minicolors-picker"></div>' +
						'</div>' + 
						'<div class="minicolors-opacity-slider">' + 
							'<div class="minicolors-picker"></div>' +
						'</div>' +
						'<div class="minicolors-grid">' +
							'<div class="minicolors-grid-inner"></div>' +
							'<div class="minicolors-picker"><div></div></div>' +
						'</div>' +
					'</div>'
				);
			
			// The swatch
			if( !settings.inline ) {
				input.after('<span class="minicolors-swatch"><span class="minicolors-swatch-color"></span></span>');
				input.next('.minicolors-swatch').on('click', function(event) {
					event.preventDefault();
					input.focus();
				});
			}
			
			// Prevent text selection in IE
			input.parent().find('.minicolors-panel').on('selectstart', function() { return false; }).end();
			
			// Inline controls
			if( settings.inline ) input.parent().addClass('minicolors-inline');
			
			updateFromInput(input, false);
			
			input.data('minicolors-initialized', true);
			
		}
		
		// Returns the input back to its original state
		function destroy(input) {
			
			var minicolors = input.parent();
			
			// Revert the input element
			input
				.removeData('minicolors-initialized')
				.removeData('minicolors-settings')
				.removeProp('size')
				.removeClass('minicolors-input');
			
			// Remove the wrap and destroy whatever remains
			minicolors.before(input).remove();
			
		}
		
		// Shows the specified dropdown panel
		function show(input) {
			
			var minicolors = input.parent(),
				panel = minicolors.find('.minicolors-panel'),
				settings = input.data('minicolors-settings');
			
			// Do nothing if uninitialized, disabled, inline, or already open
			if( !input.data('minicolors-initialized') || 
				input.prop('disabled') || 
				minicolors.hasClass('minicolors-inline') || 
				minicolors.hasClass('minicolors-focus')
			) return;
			
			hide();
			
			minicolors.addClass('minicolors-focus');
			panel
				.stop(true, true)
				.fadeIn(settings.showSpeed, function() {
					if( settings.show ) settings.show.call(input.get(0));
				});
			
		}
		
		// Hides all dropdown panels
		function hide() {
			
			$('.minicolors-focus').each( function() {
				
				var minicolors = $(this),
					input = minicolors.find('.minicolors-input'),
					panel = minicolors.find('.minicolors-panel'),
					settings = input.data('minicolors-settings');
				
				panel.fadeOut(settings.hideSpeed, function() {
					if( settings.hide ) settings.hide.call(input.get(0));
					minicolors.removeClass('minicolors-focus');
				});			
							
			});
		}
		
		// Moves the selected picker
		function move(target, event, animate) {
			
			var input = target.parents('.minicolors').find('.minicolors-input'),
				settings = input.data('minicolors-settings'),
				picker = target.find('[class$=-picker]'),
				offsetX = target.offset().left,
				offsetY = target.offset().top,
				x = Math.round(event.pageX - offsetX),
				y = Math.round(event.pageY - offsetY),
				duration = animate ? settings.animationSpeed : 0,
				wx, wy, r, phi;
				
			// Touch support
			if( event.originalEvent.changedTouches ) {
				x = event.originalEvent.changedTouches[0].pageX - offsetX;
				y = event.originalEvent.changedTouches[0].pageY - offsetY;
			}
			
			// Constrain picker to its container
			if( x < 0 ) x = 0;
			if( y < 0 ) y = 0;
			if( x > target.width() ) x = target.width();
			if( y > target.height() ) y = target.height();
			
			// Constrain color wheel values to the wheel
			if( target.parent().is('.minicolors-slider-wheel') && picker.parent().is('.minicolors-grid') ) {
				wx = 75 - x;
				wy = 75 - y;
				r = Math.sqrt(wx * wx + wy * wy);
				phi = Math.atan2(wy, wx);
				if( phi < 0 ) phi += Math.PI * 2;
				if( r > 75 ) {
					r = 75;
					x = 75 - (75 * Math.cos(phi));
					y = 75 - (75 * Math.sin(phi));
				}
				x = Math.round(x);
				y = Math.round(y);
			}
			
			// Move the picker
			if( target.is('.minicolors-grid') ) {
				picker
					.stop(true)
					.animate({
						top: y + 'px',
						left: x + 'px'
					}, duration, settings.animationEasing, function() {
						updateFromControl(input, target);
					});
			} else {
				picker
					.stop(true)
					.animate({
						top: y + 'px'
					}, duration, settings.animationEasing, function() {
						updateFromControl(input, target);
					});
			}
			
		}
		
		// Sets the input based on the color picker values
		function updateFromControl(input, target) {
			
			function getCoords(picker, container) {
				
				var left, top;
				if( !picker.length || !container ) return null;
				left = picker.offset().left;
				top = picker.offset().top;
				
				return {
					x: left - container.offset().left + (picker.outerWidth() / 2),
					y: top - container.offset().top + (picker.outerHeight() / 2)
				};
				
			}
			
			var hue, saturation, brightness, x, y, r, phi,
				
				hex = input.val(),
				opacity = input.attr('data-opacity'),
				
				// Helpful references
				minicolors = input.parent(),
				settings = input.data('minicolors-settings'),
				swatch = minicolors.find('.minicolors-swatch'),
				
				// Panel objects
				grid = minicolors.find('.minicolors-grid'),
				slider = minicolors.find('.minicolors-slider'),
				opacitySlider = minicolors.find('.minicolors-opacity-slider'),
				
				// Picker objects
				gridPicker = grid.find('[class$=-picker]'),
				sliderPicker = slider.find('[class$=-picker]'),
				opacityPicker = opacitySlider.find('[class$=-picker]'),
				
				// Picker positions
				gridPos = getCoords(gridPicker, grid),
				sliderPos = getCoords(sliderPicker, slider),
				opacityPos = getCoords(opacityPicker, opacitySlider);
			
			// Handle colors
			if( target.is('.minicolors-grid, .minicolors-slider') ) {
				
				// Determine HSB values
				switch(settings.control) {
					
					case 'wheel':
						// Calculate hue, saturation, and brightness
						x = (grid.width() / 2) - gridPos.x;
						y = (grid.height() / 2) - gridPos.y;
						r = Math.sqrt(x * x + y * y);
						phi = Math.atan2(y, x);
						if( phi < 0 ) phi += Math.PI * 2;
						if( r > 75 ) {
							r = 75;
							gridPos.x = 69 - (75 * Math.cos(phi));
							gridPos.y = 69 - (75 * Math.sin(phi));
						}
						saturation = keepWithin(r / 0.75, 0, 100);
						hue = keepWithin(phi * 180 / Math.PI, 0, 360);
						brightness = keepWithin(100 - Math.floor(sliderPos.y * (100 / slider.height())), 0, 100);
						hex = hsb2hex({
							h: hue,
							s: saturation,
							b: brightness
						});
						
						// Update UI
						slider.css('backgroundColor', hsb2hex({ h: hue, s: saturation, b: 100 }));
						break;
					
					case 'saturation':
						// Calculate hue, saturation, and brightness
						hue = keepWithin(parseInt(gridPos.x * (360 / grid.width()), 10), 0, 360);
						saturation = keepWithin(100 - Math.floor(sliderPos.y * (100 / slider.height())), 0, 100);
						brightness = keepWithin(100 - Math.floor(gridPos.y * (100 / grid.height())), 0, 100);
						hex = hsb2hex({
							h: hue,
							s: saturation,
							b: brightness
						});
						
						// Update UI
						slider.css('backgroundColor', hsb2hex({ h: hue, s: 100, b: brightness }));
						minicolors.find('.minicolors-grid-inner').css('opacity', saturation / 100);
						break;
					
					case 'brightness':
						// Calculate hue, saturation, and brightness
						hue = keepWithin(parseInt(gridPos.x * (360 / grid.width()), 10), 0, 360);
						saturation = keepWithin(100 - Math.floor(gridPos.y * (100 / grid.height())), 0, 100);
						brightness = keepWithin(100 - Math.floor(sliderPos.y * (100 / slider.height())), 0, 100);
						hex = hsb2hex({
							h: hue,
							s: saturation,
							b: brightness
						});
						
						// Update UI
						slider.css('backgroundColor', hsb2hex({ h: hue, s: saturation, b: 100 }));
						minicolors.find('.minicolors-grid-inner').css('opacity', 1 - (brightness / 100));
						break;
					
					default:
						// Calculate hue, saturation, and brightness
						hue = keepWithin(360 - parseInt(sliderPos.y * (360 / slider.height()), 10), 0, 360);
						saturation = keepWithin(Math.floor(gridPos.x * (100 / grid.width())), 0, 100);
						brightness = keepWithin(100 - Math.floor(gridPos.y * (100 / grid.height())), 0, 100);
						hex = hsb2hex({
							h: hue,
							s: saturation,
							b: brightness
						});
						
						// Update UI
						grid.css('backgroundColor', hsb2hex({ h: hue, s: 100, b: 100 }));
						break;
					
				}
			
				// Adjust case
				input.val( convertCase(hex, settings.letterCase) );
				
			}
			
			// Handle opacity
			if( target.is('.minicolors-opacity-slider') ) {
				if( settings.opacity ) {
					opacity = parseFloat(1 - (opacityPos.y / opacitySlider.height())).toFixed(2);
				} else {
					opacity = 1;
				}
				if( settings.opacity ) input.attr('data-opacity', opacity);
			}
			
			// Set swatch color
			swatch.find('SPAN').css({
				backgroundColor: hex,
				opacity: opacity
			});
			
			// Handle change event
			doChange(input, hex, opacity);
			
		}
		
		// Sets the color picker values from the input
		function updateFromInput(input, preserveInputValue) {
			
			var hex,
				hsb,
				opacity,
				x, y, r, phi,
				
				// Helpful references
				minicolors = input.parent(),
				settings = input.data('minicolors-settings'),
				swatch = minicolors.find('.minicolors-swatch'),
				
				// Panel objects
				grid = minicolors.find('.minicolors-grid'),
				slider = minicolors.find('.minicolors-slider'),
				opacitySlider = minicolors.find('.minicolors-opacity-slider'),
				
				// Picker objects
				gridPicker = grid.find('[class$=-picker]'),
				sliderPicker = slider.find('[class$=-picker]'),
				opacityPicker = opacitySlider.find('[class$=-picker]');
			
			// Determine hex/HSB values
			hex = convertCase(parseHex(input.val(), true), settings.letterCase);
			if( !hex ){
				hex = convertCase(parseHex(settings.defaultValue, true), settings.letterCase);
			}
			hsb = hex2hsb(hex);
			
			// Update input value
			if( !preserveInputValue ) input.val(hex);
			
			// Determine opacity value
			if( settings.opacity ) {
				// Get from data-opacity attribute and keep within 0-1 range
				opacity = input.attr('data-opacity') === '' ? 1 : keepWithin(parseFloat(input.attr('data-opacity')).toFixed(2), 0, 1);
				if( isNaN(opacity) ) opacity = 1;
				input.attr('data-opacity', opacity);
				swatch.find('SPAN').css('opacity', opacity);
				
				// Set opacity picker position
				y = keepWithin(opacitySlider.height() - (opacitySlider.height() * opacity), 0, opacitySlider.height());
				opacityPicker.css('top', y + 'px');
			}
			
			// Update swatch
			swatch.find('SPAN').css('backgroundColor', hex);
			
			// Determine picker locations
			switch(settings.control) {
				
				case 'wheel':
					// Set grid position
					r = keepWithin(Math.ceil(hsb.s * 0.75), 0, grid.height() / 2);
					phi = hsb.h * Math.PI / 180;
					x = keepWithin(75 - Math.cos(phi) * r, 0, grid.width());
					y = keepWithin(75 - Math.sin(phi) * r, 0, grid.height());
					gridPicker.css({
						top: y + 'px',
						left: x + 'px'
					});
					
					// Set slider position
					y = 150 - (hsb.b / (100 / grid.height()));
					if( hex === '' ) y = 0;
					sliderPicker.css('top', y + 'px');
					
					// Update panel color
					slider.css('backgroundColor', hsb2hex({ h: hsb.h, s: hsb.s, b: 100 }));
					break;
				
				case 'saturation':
					// Set grid position
					x = keepWithin((5 * hsb.h) / 12, 0, 150);
					y = keepWithin(grid.height() - Math.ceil(hsb.b / (100 / grid.height())), 0, grid.height());
					gridPicker.css({
						top: y + 'px',
						left: x + 'px'
					});				
					
					// Set slider position
					y = keepWithin(slider.height() - (hsb.s * (slider.height() / 100)), 0, slider.height());
					sliderPicker.css('top', y + 'px');
					
					// Update UI
					slider.css('backgroundColor', hsb2hex({ h: hsb.h, s: 100, b: hsb.b }));
					minicolors.find('.minicolors-grid-inner').css('opacity', hsb.s / 100);
					break;
				
				case 'brightness':
					// Set grid position
					x = keepWithin((5 * hsb.h) / 12, 0, 150);
					y = keepWithin(grid.height() - Math.ceil(hsb.s / (100 / grid.height())), 0, grid.height());
					gridPicker.css({
						top: y + 'px',
						left: x + 'px'
					});				
					
					// Set slider position
					y = keepWithin(slider.height() - (hsb.b * (slider.height() / 100)), 0, slider.height());
					sliderPicker.css('top', y + 'px');
					
					// Update UI
					slider.css('backgroundColor', hsb2hex({ h: hsb.h, s: hsb.s, b: 100 }));
					minicolors.find('.minicolors-grid-inner').css('opacity', 1 - (hsb.b / 100));
					break;
				
				default:
					// Set grid position
					x = keepWithin(Math.ceil(hsb.s / (100 / grid.width())), 0, grid.width());
					y = keepWithin(grid.height() - Math.ceil(hsb.b / (100 / grid.height())), 0, grid.height());
					gridPicker.css({
						top: y + 'px',
						left: x + 'px'
					});
					
					// Set slider position
					y = keepWithin(slider.height() - (hsb.h / (360 / slider.height())), 0, slider.height());
					sliderPicker.css('top', y + 'px');
					
					// Update panel color
					grid.css('backgroundColor', hsb2hex({ h: hsb.h, s: 100, b: 100 }));
					break;
					
			}
			
			// Fire change event, but only if minicolors is fully initialized
			if( input.data('minicolors-initialized') ) {
				doChange(input, hex, opacity);
			}
			
		}
		
		// Runs the change and changeDelay callbacks
		function doChange(input, hex, opacity) {
			
			var settings = input.data('minicolors-settings'),
				lastChange = input.data('minicolors-lastChange');
			
			// Only run if it actually changed
			if( !lastChange || lastChange.hex !== hex || lastChange.opacity !== opacity ) {
				
				// Remember last-changed value
				input.data('minicolors-lastChange', {
					hex: hex,
					opacity: opacity
				});
				
				// Fire change event
				if( settings.change ) {
					if( settings.changeDelay ) {
						// Call after a delay
						clearTimeout(input.data('minicolors-changeTimeout'));
						input.data('minicolors-changeTimeout', setTimeout( function() {
							settings.change.call(input.get(0), hex, opacity);
						}, settings.changeDelay));
					} else {
						// Call immediately
						settings.change.call(input.get(0), hex, opacity);
					}
				}
				input.trigger('change').trigger('input');
			}
		
		}
		
		// Generates an RGB(A) object based on the input's value
		function rgbObject(input) {
			var hex = parseHex($(input).val(), true),
				rgb = hex2rgb(hex),
				opacity = $(input).attr('data-opacity');
			if( !rgb ) return null;
			if( opacity !== undefined ) $.extend(rgb, { a: parseFloat(opacity) });
			return rgb;
		}
		
		// Genearates an RGB(A) string based on the input's value
		function rgbString(input, alpha) {
			var hex = parseHex($(input).val(), true),
				rgb = hex2rgb(hex),
				opacity = $(input).attr('data-opacity');
			if( !rgb ) return null;
			if( opacity === undefined ) opacity = 1;
			if( alpha ) {
				return 'rgba(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ', ' + parseFloat(opacity) + ')';
			} else {
				return 'rgb(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ')';
			}
		}
		
		// Converts to the letter case specified in settings
		function convertCase(string, letterCase) {
			return letterCase === 'uppercase' ? string.toUpperCase() : string.toLowerCase();
		}
		
		// Parses a string and returns a valid hex string when possible
		function parseHex(string, expand) {
			string = string.replace(/[^A-F0-9]/ig, '');
			if( string.length !== 3 && string.length !== 6 ) return '';
			if( string.length === 3 && expand ) {
				string = string[0] + string[0] + string[1] + string[1] + string[2] + string[2];
			}
			return '#' + string;
		}
		
		// Keeps value within min and max
		function keepWithin(value, min, max) {
			if( value < min ) value = min;
			if( value > max ) value = max;
			return value;
		}
		
		// Converts an HSB object to an RGB object
		function hsb2rgb(hsb) {
			var rgb = {};
			var h = Math.round(hsb.h);
			var s = Math.round(hsb.s * 255 / 100);
			var v = Math.round(hsb.b * 255 / 100);
			if(s === 0) {
				rgb.r = rgb.g = rgb.b = v;
			} else {
				var t1 = v;
				var t2 = (255 - s) * v / 255;
				var t3 = (t1 - t2) * (h % 60) / 60;
				if( h === 360 ) h = 0;
				if( h < 60 ) { rgb.r = t1; rgb.b = t2; rgb.g = t2 + t3; }
				else if( h < 120 ) {rgb.g = t1; rgb.b = t2; rgb.r = t1 - t3; }
				else if( h < 180 ) {rgb.g = t1; rgb.r = t2; rgb.b = t2 + t3; }
				else if( h < 240 ) {rgb.b = t1; rgb.r = t2; rgb.g = t1 - t3; }
				else if( h < 300 ) {rgb.b = t1; rgb.g = t2; rgb.r = t2 + t3; }
				else if( h < 360 ) {rgb.r = t1; rgb.g = t2; rgb.b = t1 - t3; }
				else { rgb.r = 0; rgb.g = 0; rgb.b = 0; }
			}
			return {
				r: Math.round(rgb.r),
				g: Math.round(rgb.g),
				b: Math.round(rgb.b)
			};
		}
		
		// Converts an RGB object to a hex string
		function rgb2hex(rgb) {
			var hex = [
				rgb.r.toString(16),
				rgb.g.toString(16),
				rgb.b.toString(16)
			];
			$.each(hex, function(nr, val) {
				if (val.length === 1) hex[nr] = '0' + val;
			});
			return '#' + hex.join('');
		}
		
		// Converts an HSB object to a hex string
		function hsb2hex(hsb) {
			return rgb2hex(hsb2rgb(hsb));
		}
		
		// Converts a hex string to an HSB object
		function hex2hsb(hex) {
			var hsb = rgb2hsb(hex2rgb(hex));
			if( hsb.s === 0 ) hsb.h = 360;
			return hsb;
		}
		
		// Converts an RGB object to an HSB object
		function rgb2hsb(rgb) {
			var hsb = { h: 0, s: 0, b: 0 };
			var min = Math.min(rgb.r, rgb.g, rgb.b);
			var max = Math.max(rgb.r, rgb.g, rgb.b);
			var delta = max - min;
			hsb.b = max;
			hsb.s = max !== 0 ? 255 * delta / max : 0;
			if( hsb.s !== 0 ) {
				if( rgb.r === max ) {
					hsb.h = (rgb.g - rgb.b) / delta;
				} else if( rgb.g === max ) {
					hsb.h = 2 + (rgb.b - rgb.r) / delta;
				} else {
					hsb.h = 4 + (rgb.r - rgb.g) / delta;
				}
			} else {
				hsb.h = -1;
			}
			hsb.h *= 60;
			if( hsb.h < 0 ) {
				hsb.h += 360;
			}
			hsb.s *= 100/255;
			hsb.b *= 100/255;
			return hsb;
		}
		
		// Converts a hex string to an RGB object
		function hex2rgb(hex) {
			hex = parseInt(((hex.indexOf('#') > -1) ? hex.substring(1) : hex), 16);
			return {
				r: hex >> 16,
				g: (hex & 0x00FF00) >> 8,
				b: (hex & 0x0000FF)
			};
		}
		
		// Handle events
		$(document)
			// Hide on clicks outside of the control
			.on('mousedown.minicolors touchstart.minicolors', function(event) {
				if( !$(event.target).parents().add(event.target).hasClass('minicolors') ) {
					hide();
				}
			})
			// Start moving
			.on('mousedown.minicolors touchstart.minicolors', '.minicolors-grid, .minicolors-slider, .minicolors-opacity-slider', function(event) {
				var target = $(this);
				event.preventDefault();
				$(document).data('minicolors-target', target);
				move(target, event, true);
			})
			// Move pickers
			.on('mousemove.minicolors touchmove.minicolors', function(event) {
				var target = $(document).data('minicolors-target');
				if( target ) move(target, event);
			})
			// Stop moving
			.on('mouseup.minicolors touchend.minicolors', function() {
				$(this).removeData('minicolors-target');
			})
			// Show panel when swatch is clicked
			.on('mousedown.minicolors touchstart.minicolors', '.minicolors-swatch', function(event) {
				var input = $(this).parent().find('.minicolors-input');
				event.preventDefault();
				show(input);
			})
			// Show on focus
			.on('focus.minicolors', '.minicolors-input', function() {
				var input = $(this);
				if( !input.data('minicolors-initialized') ) return;
				show(input);
			})
			// Fix hex on blur
			.on('blur.minicolors', '.minicolors-input', function() {
				var input = $(this),
					settings = input.data('minicolors-settings');
				if( !input.data('minicolors-initialized') ) return;
				
				// Parse Hex
				input.val(parseHex(input.val(), true));
				
				// Is it blank?
				if( input.val() === '' ) input.val(parseHex(settings.defaultValue, true));
				
				// Adjust case
				input.val( convertCase(input.val(), settings.letterCase) );
				
			})
			// Handle keypresses
			.on('keydown.minicolors', '.minicolors-input', function(event) {
				var input = $(this);
				if( !input.data('minicolors-initialized') ) return;
				switch(event.keyCode) {
					case 9: // tab
						hide();
						break;
					case 13: // enter
					case 27: // esc
						hide();
						input.blur();
						break;
				}
			})
			// Update on keyup
			.on('keyup.minicolors', '.minicolors-input', function() {
				var input = $(this);
				if( !input.data('minicolors-initialized') ) return;
				updateFromInput(input, true);
			})
			// Update on paste
			.on('paste.minicolors', '.minicolors-input', function() {
				var input = $(this);
				if( !input.data('minicolors-initialized') ) return;
				setTimeout( function() {
					updateFromInput(input, true);
				}, 1);
			});
		
	})(jQuery);;
	/*
	    jQuery Masked Input Plugin
	    Copyright (c) 2007 - 2013 Josh Bush (digitalbush.com)
	    Licensed under the MIT license (http://digitalbush.com/projects/masked-input-plugin/#license)
	    Version: 1.3.1
	*/
	!function($) {
	    function getPasteEvent() {
	        var el = document.createElement("input"), name = "onpaste";
	        return el.setAttribute(name, ""), "function" == typeof el[name] ? "paste" : "input";
	    }
	    var caretTimeoutId, pasteEventName = getPasteEvent() + ".mask", ua = navigator.userAgent, iPhone = /iphone/i.test(ua), chrome = /chrome/i.test(ua), android = /android/i.test(ua);
	    $.mask = {
	        definitions: {
	            "9": "[0-9]",
	            a: "[A-Za-z]",
	            "*": "[A-Za-z0-9]"
	        },
	        autoclear: !0,
	        dataName: "rawMaskFn",
	        placeholder: "_"
	    }, $.fn.extend({
	        caret: function(begin, end) {
	            var range;
	            if (0 !== this.length && !this.is(":hidden")) return "number" == typeof begin ? (end = "number" == typeof end ? end : begin, 
	            this.each(function() {
	                this.setSelectionRange ? this.setSelectionRange(begin, end) : this.createTextRange && (range = this.createTextRange(), 
	                range.collapse(!0), range.moveEnd("character", end), range.moveStart("character", begin), 
	                range.select());
	            })) : (this[0].setSelectionRange ? (begin = this[0].selectionStart, end = this[0].selectionEnd) : document.selection && document.selection.createRange && (range = document.selection.createRange(), 
	            begin = 0 - range.duplicate().moveStart("character", -1e5), end = begin + range.text.length), 
	            {
	                begin: begin,
	                end: end
	            });
	        },
	        unmask: function() {
	            return this.trigger("unmask");
	        },
	        mask: function(mask, settings) {
	            var input, defs, tests, partialPosition, firstNonMaskPos, len;
	            return !mask && this.length > 0 ? (input = $(this[0]), input.data($.mask.dataName)()) : (settings = $.extend({
	                autoclear: $.mask.autoclear,
	                placeholder: $.mask.placeholder,
	                completed: null
	            }, settings), defs = $.mask.definitions, tests = [], partialPosition = len = mask.length, 
	            firstNonMaskPos = null, $.each(mask.split(""), function(i, c) {
	                "?" == c ? (len--, partialPosition = i) : defs[c] ? (tests.push(new RegExp(defs[c])), 
	                null === firstNonMaskPos && (firstNonMaskPos = tests.length - 1)) : tests.push(null);
	            }), this.trigger("unmask").each(function() {
	                function seekNext(pos) {
	                    for (;++pos < len && !tests[pos]; ) ;
	                    return pos;
	                }
	                function seekPrev(pos) {
	                    for (;--pos >= 0 && !tests[pos]; ) ;
	                    return pos;
	                }
	                function shiftL(begin, end) {
	                    var i, j;
	                    if (!(0 > begin)) {
	                        for (i = begin, j = seekNext(end); len > i; i++) if (tests[i]) {
	                            if (!(len > j && tests[i].test(buffer[j]))) break;
	                            buffer[i] = buffer[j], buffer[j] = settings.placeholder, j = seekNext(j);
	                        }
	                        writeBuffer(), input.caret(Math.max(firstNonMaskPos, begin));
	                    }
	                }
	                function shiftR(pos) {
	                    var i, c, j, t;
	                    for (i = pos, c = settings.placeholder; len > i; i++) if (tests[i]) {
	                        if (j = seekNext(i), t = buffer[i], buffer[i] = c, !(len > j && tests[j].test(t))) break;
	                        c = t;
	                    }
	                }
	                function keydownEvent(e) {
	                    var pos, begin, end, k = e.which;
	                    8 === k || 46 === k || iPhone && 127 === k ? (pos = input.caret(), begin = pos.begin, 
	                    end = pos.end, 0 === end - begin && (begin = 46 !== k ? seekPrev(begin) : end = seekNext(begin - 1), 
	                    end = 46 === k ? seekNext(end) : end), clearBuffer(begin, end), shiftL(begin, end - 1), 
	                    e.preventDefault()) : 27 == k && (input.val(focusText), input.caret(0, checkVal()), 
	                    e.preventDefault());
	                }
	                function keypressEvent(e) {
	                    var p, c, next, k = e.which, pos = input.caret();
	                    if (0 == k) {
	                        if (pos.begin >= len) return input.val(input.val().substr(0, len)), e.preventDefault(), 
	                        !1;
	                        pos.begin == pos.end && (k = input.val().charCodeAt(pos.begin - 1), pos.begin--, 
	                        pos.end--);
	                    }
	                    e.ctrlKey || e.altKey || e.metaKey || 32 > k || k && (0 !== pos.end - pos.begin && (clearBuffer(pos.begin, pos.end), 
	                    shiftL(pos.begin, pos.end - 1)), p = seekNext(pos.begin - 1), len > p && (c = String.fromCharCode(k), 
	                    tests[p].test(c) && (shiftR(p), buffer[p] = c, writeBuffer(), next = seekNext(p), 
	                    android ? setTimeout($.proxy($.fn.caret, input, next), 0) : input.caret(next), settings.completed && next >= len && settings.completed.call(input))), 
	                    e.preventDefault());
	                }
	                function clearBuffer(start, end) {
	                    var i;
	                    for (i = start; end > i && len > i; i++) tests[i] && (buffer[i] = settings.placeholder);
	                }
	                function writeBuffer() {
	                    input.val(buffer.join(""));
	                }
	                function checkVal(allow) {
	                    var i, c, pos, test = input.val(), lastMatch = -1;
	                    for (i = 0, pos = 0; len > i; i++) if (tests[i]) {
	                        for (buffer[i] = settings.placeholder; pos++ < test.length; ) if (c = test.charAt(pos - 1), 
	                        tests[i].test(c)) {
	                            buffer[i] = c, lastMatch = i;
	                            break;
	                        }
	                        if (pos > test.length) break;
	                    } else buffer[i] === test.charAt(pos) && i !== partialPosition && (pos++, lastMatch = i);
	                    return allow ? writeBuffer() : partialPosition > lastMatch + 1 ? settings.autoclear || buffer.join("") === defaultBuffer ? (input.val(""), 
	                    clearBuffer(0, len)) : writeBuffer() : (writeBuffer(), input.val(input.val().substring(0, lastMatch + 1))), 
	                    partialPosition ? i : firstNonMaskPos;
	                }
	                var input = $(this), buffer = $.map(mask.split(""), function(c) {
	                    return "?" != c ? defs[c] ? settings.placeholder : c : void 0;
	                }), defaultBuffer = buffer.join(""), focusText = input.val();
	                input.data($.mask.dataName, function() {
	                    return $.map(buffer, function(c, i) {
	                        return tests[i] && c != settings.placeholder ? c : null;
	                    }).join("");
	                }), input.attr("readonly") || input.one("unmask", function() {
	                    input.unbind(".mask").removeData($.mask.dataName);
	                }).bind("focus.mask", function() {
	                    clearTimeout(caretTimeoutId);
	                    var pos;
	                    focusText = input.val(), pos = checkVal(), caretTimeoutId = setTimeout(function() {
	                        writeBuffer(), pos == mask.length ? input.caret(0, pos) : input.caret(pos);
	                    }, 10);
	                }).bind("blur.mask", function() {
	                    checkVal(), input.val() != focusText && input.change();
	                }).bind("keydown.mask", keydownEvent).bind("keypress.mask", keypressEvent).bind(pasteEventName, function() {
	                    setTimeout(function() {
	                        var pos = checkVal(!0);
	                        input.caret(pos), settings.completed && pos == input.val().length && settings.completed.call(input);
	                    }, 0);
	                }), chrome && android && input.bind("keyup.mask", keypressEvent), checkVal();
	            }));
	        }
	    });
	}(jQuery);;
	/*!
		Autosize v1.18.4 - 2014-01-11
		Automatically adjust textarea height based on user input.
		(c) 2014 Jack Moore - http://www.jacklmoore.com/autosize
		license: http://www.opensource.org/licenses/mit-license.php
	*/
	(function ($) {
		var
		defaults = {
			className: 'autosizejs',
			append: '',
			callback: false,
			resizeDelay: 10,
			placeholder: true
		},

		// border:0 is unnecessary, but avoids a bug in Firefox on OSX
		copy = '<textarea tabindex="-1" style="position:absolute; top:-999px; left:0; right:auto; bottom:auto; border:0; padding: 0; -moz-box-sizing:content-box; -webkit-box-sizing:content-box; box-sizing:content-box; word-wrap:break-word; height:0 !important; min-height:0 !important; overflow:hidden; transition:none; -webkit-transition:none; -moz-transition:none;"/>',

		// line-height is conditionally included because IE7/IE8/old Opera do not return the correct value.
		typographyStyles = [
			'fontFamily',
			'fontSize',
			'fontWeight',
			'fontStyle',
			'letterSpacing',
			'textTransform',
			'wordSpacing',
			'textIndent'
		],

		// to keep track which textarea is being mirrored when adjust() is called.
		mirrored,

		// the mirror element, which is used to calculate what size the mirrored element should be.
		mirror = $(copy).data('autosize', true)[0];

		// test that line-height can be accurately copied.
		mirror.style.lineHeight = '99px';
		if ($(mirror).css('lineHeight') === '99px') {
			typographyStyles.push('lineHeight');
		}
		mirror.style.lineHeight = '';

		$.fn.autosize = function (options) {
			if (!this.length) {
				return this;
			}

			options = $.extend({}, defaults, options || {});

			if (mirror.parentNode !== document.body) {
				$(document.body).append(mirror);
			}

			return this.each(function () {
				var
				ta = this,
				$ta = $(ta),
				maxHeight,
				minHeight,
				boxOffset = 0,
				callback = $.isFunction(options.callback),
				originalStyles = {
					height: ta.style.height,
					overflow: ta.style.overflow,
					overflowY: ta.style.overflowY,
					wordWrap: ta.style.wordWrap,
					resize: ta.style.resize
				},
				timeout,
				width = $ta.width();

				if ($ta.data('autosize')) {
					// exit if autosize has already been applied, or if the textarea is the mirror element.
					return;
				}
				$ta.data('autosize', true);

				if ($ta.css('box-sizing') === 'border-box' || $ta.css('-moz-box-sizing') === 'border-box' || $ta.css('-webkit-box-sizing') === 'border-box'){
					boxOffset = $ta.outerHeight() - $ta.height();
				}

				// IE8 and lower return 'auto', which parses to NaN, if no min-height is set.
				minHeight = Math.max(parseInt($ta.css('minHeight'), 10) - boxOffset || 0, $ta.height());

				$ta.css({
					overflow: 'hidden',
					overflowY: 'hidden',
					wordWrap: 'break-word', // horizontal overflow is hidden, so break-word is necessary for handling words longer than the textarea width
					resize: ($ta.css('resize') === 'none' || $ta.css('resize') === 'vertical') ? 'none' : 'horizontal'
				});

				// The mirror width must exactly match the textarea width, so using getBoundingClientRect because it doesn't round the sub-pixel value.
				// window.getComputedStyle, getBoundingClientRect returning a width are unsupported, but also unneeded in IE8 and lower.
				function setWidth() {
					var width;
					var style = window.getComputedStyle ? window.getComputedStyle(ta, null) : false;
					
					if (style) {

						width = ta.getBoundingClientRect().width;

						if (width === 0) {
							width = parseInt(style.width,10);
						}

						$.each(['paddingLeft', 'paddingRight', 'borderLeftWidth', 'borderRightWidth'], function(i,val){
							width -= parseInt(style[val],10);
						});
					} else {
						width = Math.max($ta.width(), 0);
					}

					mirror.style.width = width + 'px';
				}

				function initMirror() {
					var styles = {};

					mirrored = ta;
					mirror.className = options.className;
					maxHeight = parseInt($ta.css('maxHeight'), 10);

					// mirror is a duplicate textarea located off-screen that
					// is automatically updated to contain the same text as the
					// original textarea.  mirror always has a height of 0.
					// This gives a cross-browser supported way getting the actual
					// height of the text, through the scrollTop property.
					$.each(typographyStyles, function(i,val){
						styles[val] = $ta.css(val);
					});
					$(mirror).css(styles);

					setWidth();

					// Chrome-specific fix:
					// When the textarea y-overflow is hidden, Chrome doesn't reflow the text to account for the space
					// made available by removing the scrollbar. This workaround triggers the reflow for Chrome.
					if (window.chrome) {
						var width = ta.style.width;
						ta.style.width = '0px';
						var ignore = ta.offsetWidth;
						ta.style.width = width;
					}
				}

				// Using mainly bare JS in this function because it is going
				// to fire very often while typing, and needs to very efficient.
				function adjust() {
					var height, original;

					if (mirrored !== ta) {
						initMirror();
					} else {
						setWidth();
					}

					if (!ta.value && options.placeholder) {
						// If the textarea is empty, copy the placeholder text into 
						// the mirror control and use that for sizing so that we 
						// don't end up with placeholder getting trimmed.
						mirror.value = ($(ta).attr("placeholder") || '') + options.append;
					} else {
						mirror.value = ta.value + options.append;
					}

					mirror.style.overflowY = ta.style.overflowY;
					original = parseInt(ta.style.height,10);

					// Setting scrollTop to zero is needed in IE8 and lower for the next step to be accurately applied
					mirror.scrollTop = 0;

					mirror.scrollTop = 9e4;

					// Using scrollTop rather than scrollHeight because scrollHeight is non-standard and includes padding.
					height = mirror.scrollTop;

					if (maxHeight && height > maxHeight) {
						ta.style.overflowY = 'scroll';
						height = maxHeight;
					} else {
						ta.style.overflowY = 'hidden';
						if (height < minHeight) {
							height = minHeight;
						}
					}

					height += boxOffset;

					if (original !== height) {
						ta.style.height = height + 'px';
						if (callback) {
							options.callback.call(ta,ta);
						}
					}
				}

				function resize () {
					clearTimeout(timeout);
					timeout = setTimeout(function(){
						var newWidth = $ta.width();

						if (newWidth !== width) {
							width = newWidth;
							adjust();
						}
					}, parseInt(options.resizeDelay,10));
				}

				if ('onpropertychange' in ta) {
					if ('oninput' in ta) {
						// Detects IE9.  IE9 does not fire onpropertychange or oninput for deletions,
						// so binding to onkeyup to catch most of those occasions.  There is no way that I
						// know of to detect something like 'cut' in IE9.
						$ta.on('input.autosize keyup.autosize', adjust);
					} else {
						// IE7 / IE8
						$ta.on('propertychange.autosize', function(){
							if(event.propertyName === 'value'){
								adjust();
							}
						});
					}
				} else {
					// Modern Browsers
					$ta.on('input.autosize', adjust);
				}

				// Set options.resizeDelay to false if using fixed-width textarea elements.
				// Uses a timeout and width check to reduce the amount of times adjust needs to be called after window resize.

				if (options.resizeDelay !== false) {
					$(window).on('resize.autosize', resize);
				}

				// Event for manual triggering if needed.
				// Should only be needed when the value of the textarea is changed through JavaScript rather than user input.
				$ta.on('autosize.resize', adjust);

				// Event for manual triggering that also forces the styles to update as well.
				// Should only be needed if one of typography styles of the textarea change, and the textarea is already the target of the adjust method.
				$ta.on('autosize.resizeIncludeStyle', function() {
					mirrored = null;
					adjust();
				});

				$ta.on('autosize.destroy', function(){
					mirrored = null;
					clearTimeout(timeout);
					$(window).off('resize', resize);
					$ta
						.off('autosize')
						.off('.autosize')
						.css(originalStyles)
						.removeData('autosize');
				});

				// Call adjust in case the textarea already contains text.
				adjust();
			});
		};
	}(window.jQuery || window.$)); // jQuery or jQuery-like library, such as Zepto
	;

	
	
/**
 * COMMON ADMIN UI DEFINE
 */
(function($, undefined) {
	var common = window.common = window.common || {};
	common.ui =  common.ui || {},
	common.ui.admin = common.ui.admin || {};
	
	var kendo = window.kendo,
	Widget = kendo.ui.Widget,
	Class = kendo.Class,
	stringify = kendo.stringify,
	isFunction = kendo.isFunction,
	UNDEFINED = 'undefined',
	POST = 'POST',
	OBJECT_TYPE = 30 ,
	JSON = 'json';
		
	common.ui.admin.Setup = kendo.Class.extend({		
		init : function (options){
			options = options || {};			
			var that = this;
			that.options = options;
			
			options.localStorageSupported = typeof window.Storage !== "undefined" ? true : false;
			
			that._pixelAdmin = window.PixelAdmin;
			
			that.refresh();
		},		
		refresh: function(){			
			var that = this;
			$('.menu-content-profile .close').click(function () {
				var $p = $(this).parents('.menu-content');
				$p.addClass('fadeOut');
				setTimeout(function () {
					$p.css({ height: $p.outerHeight(), overflow: 'hidden' }).animate({'padding-top': 0, height: $('#main-navbar').outerHeight()}, 500, function () {
						$p.remove();
					});
				}, 300);
				return false;
			});
			
			var initilizer, _i, _len, _ref;		
			$('html').addClass('pxajs');
			that._pixelAdmin.setting = {
				    is_mobile: false,
				    resize_delay: 400,
				    stored_values_prefix: 'pa_',
				    main_menu: {
				      accordion: true,
				      animation_speed: 250,
				      store_state: true,
				      store_state_key: 'mmstate',
				      disable_animation_on: [],
				      dropdown_close_delay: 300,
				      detect_active: true,
				      detect_active_predicate: function(href, url) {
				        return href === url;
				      }
				    },
				    consts: {
				      COLORS: ['#71c73e', '#77b7c5', '#d54848', '#6c42e5', '#e8e64e', '#dd56e6', '#ecad3f', '#618b9d', '#b68b68', '#36a766', '#3156be', '#00b3ff', '#646464', '#a946e8', '#9d9d9d']
				    }
				  };
			that._pixelAdmin.settings.is_mobile = /iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase());
	        if (that._pixelAdmin.settings.is_mobile) {
	        	if (FastClick) {
	        		FastClick.attach(document.body);
	        	}
	        }	
	        _ref = that._pixelAdmin.init;
	        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
		          initilizer = _ref[_i];
		          $.proxy(initilizer, that._pixelAdmin);
		    }
			$(window).trigger("pa.loaded");
			$(window).resize();
		
		}
	});		
	
	
	
	
})(jQuery);


common.ui.admin.setup = function (options){
	options = options || {};			
	return new common.ui.admin.Setup(options);	
}
	