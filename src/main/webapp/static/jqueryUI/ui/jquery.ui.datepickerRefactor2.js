/*
 * jQuery UI Datepicker 1.8.10
 *
 * Copyright 2011, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Datepicker
 *
 * Depends:
 *	jquery.ui.core.js
 */
var $targetVal = "";
var $disableVals = "";
var $modVals = "";
var index = 0;
(function( $, undefined ) {

	$.extend($.ui, { datepicker: { version: "1.8.10" } });

	var PROP_NAME = 'datepicker';
	var dpuuid = new Date().getTime();
	var passCtrl = false;

	/* Date picker manager.
	 Use the singleton instance of this class, $.datepicker, to interact with the date picker.
	 Settings for (groups of) date pickers are maintained in an instance object,
	 allowing multiple different settings on the same page. */

	function Datepicker() {
		this.debug = false; // Change this to true to start debugging
		this._curInst = null; // The current instance in use
		this._keyEvent = false; // If the last event was a key event
		this._disabledInputs = []; // List of date picker inputs that have been disabled
		this._datepickerShowing = false; // True if the popup picker is showing , false if not
		this._inDialog = false; // True if showing within a "dialog", false if not
		this._mainDivId = 'ui-datepicker-div'; // The ID of the main datepicker division
		this._inlineClass = 'ui-datepicker-inline'; // The name of the inline marker class
		this._appendClass = 'ui-datepicker-append'; // The name of the append marker class
		this._triggerClass = 'ui-datepicker-trigger'; // The name of the trigger marker class
		this._dialogClass = 'ui-datepicker-dialog'; // The name of the dialog marker class
		this._disableClass = 'ui-datepicker-disabled'; // The name of the disabled covering marker class
		this._unselectableClass = 'ui-datepicker-unselectable'; // The name of the unselectable cell marker class
		this._currentClass = 'ui-datepicker-current-day'; // The name of the current day marker class
		this._dayOverClass = 'ui-datepicker-days-cell-over'; // The name of the day hover marker class
		this.regional = []; // Available regional settings, indexed by language code
		this.regional[''] = { // Default regional settings
			closeText: 'Done', // Display text for close link
			prevText: 'Prev', // Display text for previous month link
			nextText: 'Next', // Display text for next month link
			currentText: 'Today', // Display text for current month link
			monthNames: ['一月','二月','三月','四月','五月','六月',
				'七月','八月','九月','十月','十一月','十二月'], // Names of months for drop-down and formatting
			monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'], // For formatting
			dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'], // For formatting
			dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'], // For formatting
			dayNamesMin: ['Su','Mo','Tu','We','Th','Fr','Sa'], // Column headings for days starting at Sunday
			weekHeader: 'Wk', // Column header for week of the year
			dateFormat: 'mm/dd/yy', // See format options on parseDate
			firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
			isRTL: false, // True if right-to-left language, false if left-to-right
			showMonthAfterYear: false, // True if the year select precedes month, false for month then year
			yearSuffix: '' // Additional text to append to the year in the month headers
		};
		this._defaults = { // Global defaults for all the date picker instances
			showOn: 'focus', // 'focus' for popup on focus,
			// 'button' for trigger button, or 'both' for either
			showAnim: '', // Name of jQuery animation for popup
			showOptions: {}, // Options for enhanced animations
			defaultDate: null, // Used when field is blank: actual date,
			// +/-number for offset from today, null for today
			appendText: '', // Display text following the input box, e.g. showing the format
			buttonText: '...', // Text for trigger button
			buttonImage: '', // URL for trigger button image
			buttonImageOnly: false, // True if the image appears alone, false if it appears on a button
			hideIfNoPrevNext: false, // True to hide next/previous month links
			// if not applicable, false to just disable them
			navigationAsDateFormat: false, // True if date formatting applied to prev/today/next links
			gotoCurrent: false, // True if today link goes back to current selection instead
			changeMonth: false, // True if month can be selected directly, false if only prev/next
			changeYear: false, // True if year can be selected directly, false if only prev/next
			yearRange: 'c-10:c+10', // Range of years to display in drop-down,
			// either relative to today's year (-nn:+nn), relative to currently displayed year
			// (c-nn:c+nn), absolute (nnnn:nnnn), or a combination of the above (nnnn:-n)
			showOtherMonths: true, // True to show dates in other months, false to leave blank
			selectOtherMonths: false, // True to allow selection of dates in other months, false for unselectable
			showWeek: false, // True to show week of the year, false to not show it
			calculateWeek: this.iso8601Week, // How to calculate the week of the year,
			// takes a Date and returns the number of the week for it
			shortYearCutoff: '+10', // Short year values < this are in the current century,
			// > this are in the previous century,
			// string value starting with '+' for current year + value
			minDate: null, // The earliest selectable date, or null for no limit
			maxDate: null, // The latest selectable date, or null for no limit
			duration: 'fast', // Duration of display/closure
			beforeShowDay: null, // Function that takes a date and returns an array with
			// [0] = true if selectable, false if not, [1] = custom CSS class name(s) or '',
			// [2] = cell title (optional), e.g. $.datepicker.noWeekends
			beforeShow: null, // Function that takes an input field and
			// returns a set of custom settings for the date picker
			onSelect: null, // Define a callback function when a date is selected
			onChangeMonthYear: null, // Define a callback function when the month or year is changed
			onClose: null, // Define a callback function when the datepicker is closed
			numberOfMonths: 2, // Number of months to show at a time
			showCurrentAtPos: 0, // The position in multipe months at which to show the current month (starting at 0)
			stepMonths: 1, // Number of months to step back/forward
			stepBigMonths: 12, // Number of months to step back/forward for the big links
			altField: '', // Selector for an alternate field to store selected dates into
			altFormat: '', // The date format to use for the alternate field
			constrainInput: true, // The input is constrained by the current date format
			showButtonPanel: false, // True to show button panel, false to not show it
			autoSize: false // True to size the input for the date format, false to leave as is
		};
		$.extend(this._defaults, this.regional['']);
		this.dpDiv = $('<div id="' + this._mainDivId + '" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"></div>');

		$(document).keydown(function(event){
			if(event.keyCode == 17 || event.keyCode == 16){
				passCtrl = true;
			}
		}).keyup(function(){
			passCtrl = false;
		})
	}

	$.extend(Datepicker.prototype, {
		/* Class name added to elements to indicate already configured with a date picker. */
		markerClassName: 'hasDatepicker',

		/* Debug logging (if enabled). */
		log: function () {
			if (this.debug)
				console.log.apply('', arguments);
		},

		// TODO rename to "widget" when switching to widget factory
		_widgetDatepicker: function() {
			return this.dpDiv;
		},

		/* Override the default settings for all instances of the date picker.
		 @param  settings  object - the new settings to use as defaults (anonymous object)
		 @return the manager object */
		setDefaults: function(settings) {
			extendRemove(this._defaults, settings || {});
			return this;
		},


		/* Attach the date picker to a jQuery selection.
		 @param  target    element - the target input field or division or span
		 @param  settings  object - the new settings to use for this date picker instance (anonymous) */
		_attachDatepicker: function(target, settings) {
			// check for settings on the control itself - in namespace 'date:'
			var inlineSettings = null;
			for (var attrName in this._defaults) {
				var attrValue = target.getAttribute('date:' + attrName);
				if (attrValue) {
					inlineSettings = inlineSettings || {};
					try {
						inlineSettings[attrName] = eval(attrValue);
					} catch (err) {
						inlineSettings[attrName] = attrValue;
					}
				}
			}
			var nodeName = target.nodeName.toLowerCase();
			var inline = (nodeName == 'div' || nodeName == 'span');
			if (!target.id) {
				this.uuid += 1;
				target.id = 'dp' + this.uuid;
			}
			var inst = this._newInst($(target), inline);
			inst.settings = $.extend({}, settings || {}, inlineSettings || {});
			if (nodeName == 'input') {
				this._connectDatepicker(target, inst);
			} else if (inline) {
				this._inlineDatepicker(target, inst);
			}
		},

		/* Create a new instance object. */
		_newInst: function(target, inline) {
			var id = target[0].id.replace(/([^A-Za-z0-9_-])/g, '\\\\$1'); // escape jQuery meta chars
			return {id: id, input: target, // associated target
				selectedDay: 0, selectedMonth: 0, selectedYear: 0, // current selection
				drawMonth: 0, drawYear: 0, // month being drawn
				inline: inline, // is datepicker inline or not
				dpDiv: (!inline ? this.dpDiv : // presentation div
						$('<div class="' + this._inlineClass + ' ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"></div>'))};
		},

		/* Attach the date picker to an input field. */
		_connectDatepicker: function(target, inst) {
			var input = $(target);
			inst.append = $([]);
			inst.trigger = $([]);
			if (input.hasClass(this.markerClassName))
				return;
			this._attachments(input, inst);
			input.addClass(this.markerClassName).keydown(this._doKeyDown).
			keypress(this._doKeyPress).keyup(this._doKeyUp).
			bind("setData.datepicker", function(event, key, value) {
				inst.settings[key] = value;
			}).bind("getData.datepicker", function(event, key) {
				return this._get(inst, key);
			});
			this._autoSize(inst);
			$.data(target, PROP_NAME, inst);
		},

		/* Make attachments based on settings. */
		_attachments: function(input, inst) {
			var appendText = this._get(inst, 'appendText');
			var isRTL = this._get(inst, 'isRTL');
			if (inst.append)
				inst.append.remove();
			if (appendText) {
				inst.append = $('<span class="' + this._appendClass + '">' + appendText + '</span>');
				input[isRTL ? 'before' : 'after'](inst.append);
			}
			input.unbind('focus', this._showDatepicker);
			if (inst.trigger)
				inst.trigger.remove();
			var showOn = this._get(inst, 'showOn');
			if (showOn == 'focus' || showOn == 'both') // pop-up date picker when in the marked field
				input.focus(this._showDatepicker);
			if (showOn == 'button' || showOn == 'both') { // pop-up date picker when button clicked
				var buttonText = this._get(inst, 'buttonText');
				var buttonImage = this._get(inst, 'buttonImage');
				inst.trigger = $(this._get(inst, 'buttonImageOnly') ?
						$('<img/>').addClass(this._triggerClass).
						attr({ src: buttonImage, alt: buttonText, title: buttonText }) :
						$('<button type="button"></button>').addClass(this._triggerClass).
						html(buttonImage == '' ? buttonText : $('<img/>').attr(
								{ src:buttonImage, alt:buttonText, title:buttonText })));
				input[isRTL ? 'before' : 'after'](inst.trigger);
				inst.trigger.click(function() {
					if ($.datepicker._datepickerShowing && $.datepicker._lastInput == input[0])
						$.datepicker._hideDatepicker();
					else
						$.datepicker._showDatepicker(input[0]);
					return false;
				});
			}
		},

		/* Apply the maximum length for the date format. */
		_autoSize: function(inst) {
			if (this._get(inst, 'autoSize') && !inst.inline) {
				var date = new Date(2009, 12 - 1, 20); // Ensure double digits
				var dateFormat = this._get(inst, 'dateFormat');
				if (dateFormat.match(/[DM]/)) {
					var findMax = function(names) {
						var max = 0;
						var maxI = 0;
						for (var i = 0; i < names.length; i++) {
							if (names[i].length > max) {
								max = names[i].length;
								maxI = i;
							}
						}
						return maxI;
					};
					date.setMonth(findMax(this._get(inst, (dateFormat.match(/MM/) ?
							'monthNames' : 'monthNamesShort'))));
					date.setDate(findMax(this._get(inst, (dateFormat.match(/DD/) ?
									'dayNames' : 'dayNamesShort'))) + 20 - date.getDay());
				}
				inst.input.attr('size', this._formatDate(inst, date).length);
			}
		},

		/* Attach an inline date picker to a div. */
		_inlineDatepicker: function(target, inst) {
			var divSpan = $(target);
			if (divSpan.hasClass(this.markerClassName))
				return;
			divSpan.addClass(this.markerClassName).append(inst.dpDiv).
			bind("setData.datepicker", function(event, key, value){
				inst.settings[key] = value;
			}).bind("getData.datepicker", function(event, key){
				return this._get(inst, key);
			});
			$.data(target, PROP_NAME, inst);
			this._setDate(inst, this._getDefaultDate(inst), true);
			this._updateDatepicker(inst);
			this._updateAlternate(inst);
			inst.dpDiv.show();
		},

		/* Pop-up the date picker in a "dialog" box.
		 @param  input     element - ignored
		 @param  date      string or Date - the initial date to display
		 @param  onSelect  function - the function to call when a date is selected
		 @param  settings  object - update the dialog date picker instance's settings (anonymous object)
		 @param  pos       int[2] - coordinates for the dialog's position within the screen or
		 event - with x/y coordinates or
		 leave empty for default (screen centre)
		 @return the manager object */
		_dialogDatepicker: function(input, date, onSelect, settings, pos) {
			var inst = this._dialogInst; // internal instance
			if (!inst) {
				this.uuid += 1;
				var id = 'dp' + this.uuid;
				this._dialogInput = $('<input type="text" id="' + id +
						'" style="position: absolute; top: -100px; width: 0px; z-index: -10;"/>');
				this._dialogInput.keydown(this._doKeyDown);
				$('body').append(this._dialogInput);
				inst = this._dialogInst = this._newInst(this._dialogInput, false);
				inst.settings = {};
				$.data(this._dialogInput[0], PROP_NAME, inst);
			}
			extendRemove(inst.settings, settings || {});
			date = (date && date.constructor == Date ? this._formatDate(inst, date) : date);
			this._dialogInput.val(date);

			this._pos = (pos ? (pos.length ? pos : [pos.pageX, pos.pageY]) : null);
			if (!this._pos) {
				var browserWidth = document.documentElement.clientWidth;
				var browserHeight = document.documentElement.clientHeight;
				var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
				var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
				this._pos = // should use actual width/height below
						[(browserWidth / 2) - 100 + scrollX, (browserHeight / 2) - 150 + scrollY];
			}

			// move input on screen for focus, but hidden behind dialog
			this._dialogInput.css('left', (this._pos[0] + 20) + 'px').css('top', this._pos[1] + 'px');
			inst.settings.onSelect = onSelect;
			this._inDialog = true;
			this.dpDiv.addClass(this._dialogClass);
			this._showDatepicker(this._dialogInput[0]);
			if ($.blockUI)
				$.blockUI(this.dpDiv);
			$.data(this._dialogInput[0], PROP_NAME, inst);
			return this;
		},

		/* Detach a datepicker from its control.
		 @param  target    element - the target input field or division or span */
		_destroyDatepicker: function(target) {
			var $target = $(target);
			var inst = $.data(target, PROP_NAME);
			if (!$target.hasClass(this.markerClassName)) {
				return;
			}
			var nodeName = target.nodeName.toLowerCase();
			$.removeData(target, PROP_NAME);
			if (nodeName == 'input') {
				inst.append.remove();
				inst.trigger.remove();
				$target.removeClass(this.markerClassName).
				unbind('focus', this._showDatepicker).
				unbind('keydown', this._doKeyDown).
				unbind('keypress', this._doKeyPress).
				unbind('keyup', this._doKeyUp);
			} else if (nodeName == 'div' || nodeName == 'span')
				$target.removeClass(this.markerClassName).empty();
		},

		/* Enable the date picker to a jQuery selection.
		 @param  target    element - the target input field or division or span */
		_enableDatepicker: function(target) {
			var $target = $(target);
			var inst = $.data(target, PROP_NAME);
			if (!$target.hasClass(this.markerClassName)) {
				return;
			}
			var nodeName = target.nodeName.toLowerCase();
			if (nodeName == 'input') {
				target.disabled = false;
				inst.trigger.filter('button').
				each(function() { this.disabled = false; }).end().
				filter('img').css({opacity: '1.0', cursor: ''});
			}
			else if (nodeName == 'div' || nodeName == 'span') {
				var inline = $target.children('.' + this._inlineClass);
				inline.children().removeClass('ui-state-disabled');
			}
			this._disabledInputs = $.map(this._disabledInputs,
					function(value) { return (value == target ? null : value); }); // delete entry
		},

		/* Disable the date picker to a jQuery selection.
		 @param  target    element - the target input field or division or span */
		_disableDatepicker: function(target) {
			var $target = $(target);
			var inst = $.data(target, PROP_NAME);
			if (!$target.hasClass(this.markerClassName)) {
				return;
			}
			var nodeName = target.nodeName.toLowerCase();
			if (nodeName == 'input') {
				target.disabled = true;
				inst.trigger.filter('button').
				each(function() { this.disabled = true; }).end().
				filter('img').css({opacity: '0.5', cursor: 'default'});
			}
			else if (nodeName == 'div' || nodeName == 'span') {
				var inline = $target.children('.' + this._inlineClass);
				inline.children().addClass('ui-state-disabled');
			}
			this._disabledInputs = $.map(this._disabledInputs,
					function(value) { return (value == target ? null : value); }); // delete entry
			this._disabledInputs[this._disabledInputs.length] = target;
		},

		/* Is the first field in a jQuery collection disabled as a datepicker?
		 @param  target    element - the target input field or division or span
		 @return boolean - true if disabled, false if enabled */
		_isDisabledDatepicker: function(target) {
			if (!target) {
				return false;
			}
			for (var i = 0; i < this._disabledInputs.length; i++) {
				if (this._disabledInputs[i] == target)
					return true;
			}
			return false;
		},

		/* Retrieve the instance data for the target control.
		 @param  target  element - the target input field or division or span
		 @return  object - the associated instance data
		 @throws  error if a jQuery problem getting data */
		_getInst: function(target) {
			try {
				return $.data(target, PROP_NAME);
			}
			catch (err) {
				throw 'Missing instance data for this datepicker';
			}
		},

		/* Update or retrieve the settings for a date picker attached to an input field or division.
		 @param  target  element - the target input field or division or span
		 @param  name    object - the new settings to update or
		 string - the name of the setting to change or retrieve,
		 when retrieving also 'all' for all instance settings or
		 'defaults' for all global defaults
		 @param  value   any - the new value for the setting
		 (omit if above is an object or to retrieve a value) */
		_optionDatepicker: function(target, name, value) {
			var inst = this._getInst(target);
			if (arguments.length == 2 && typeof name == 'string') {
				return (name == 'defaults' ? $.extend({}, $.datepicker._defaults) :
						(inst ? (name == 'all' ? $.extend({}, inst.settings) :
								this._get(inst, name)) : null));
			}
			var settings = name || {};
			if (typeof name == 'string') {
				settings = {};
				settings[name] = value;
			}
			if (inst) {
				if (this._curInst == inst) {
					this._hideDatepicker();
				}
				var date = this._getDateDatepicker(target, true);
				extendRemove(inst.settings, settings);
				this._attachments($(target), inst);
				this._autoSize(inst);
				this._setDateDatepicker(target, date);
				this._updateDatepicker(inst);
			}
		},

		// change method deprecated
		_changeDatepicker: function(target, name, value) {
			this._optionDatepicker(target, name, value);
		},

		/* Redraw the date picker attached to an input field or division.
		 @param  target  element - the target input field or division or span */
		_refreshDatepicker: function(target) {
			var inst = this._getInst(target);
			if (inst) {
				this._updateDatepicker(inst);
			}
		},

		/* Set the dates for a jQuery selection.
		 @param  target   element - the target input field or division or span
		 @param  date     Date - the new date */
		_setDateDatepicker: function(target, date) {
			var inst = this._getInst(target);
			if (inst) {
				this._setDate(inst, date);
				this._updateDatepicker(inst);
				this._updateAlternate(inst);
			}
		},

		/* Get the date(s) for the first entry in a jQuery selection.
		 @param  target     element - the target input field or division or span
		 @param  noDefault  boolean - true if no default date is to be used
		 @return Date - the current date */
		_getDateDatepicker: function(target, noDefault) {
			var inst = this._getInst(target);
			if (inst && !inst.inline)
				this._setDateFromField(inst, noDefault);
			return (inst ? this._getDate(inst) : null);
		},

		/* Handle keystrokes. */
		_doKeyDown: function(event) {
			var inst = $.datepicker._getInst(event.target);
			var handled = true;
			var isRTL = inst.dpDiv.is('.ui-datepicker-rtl');
			inst._keyEvent = true;
			if ($.datepicker._datepickerShowing)
				switch (event.keyCode) {
					case 9: $.datepicker._hideDatepicker();
						handled = false;
						break; // hide on tab out
					case 13: var sel = $('td.' + $.datepicker._dayOverClass + ':not(.' +
							$.datepicker._currentClass + ')', inst.dpDiv);
						if (sel[0])
							$.datepicker._selectDay(event.target, inst.selectedMonth, inst.selectedYear, sel[0]);
						else
							$.datepicker._hideDatepicker();
						return false; // don't submit the form
						break; // select the value on enter
					case 27: $.datepicker._hideDatepicker();
						break; // hide on escape
					case 33: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
							-$.datepicker._get(inst, 'stepBigMonths') :
							-$.datepicker._get(inst, 'stepMonths')), 'M');
						break; // previous month/year on page up/+ ctrl
					case 34: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
							+$.datepicker._get(inst, 'stepBigMonths') :
							+$.datepicker._get(inst, 'stepMonths')), 'M');
						break; // next month/year on page down/+ ctrl
					case 35: if (event.ctrlKey || event.metaKey) $.datepicker._clearDate(event.target);
						handled = event.ctrlKey || event.metaKey;
						break; // clear on ctrl or command +end
					case 36: if (event.ctrlKey || event.metaKey) $.datepicker._gotoToday(event.target);
						handled = event.ctrlKey || event.metaKey;
						break; // current on ctrl or command +home
					case 37: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, (isRTL ? +1 : -1), 'D');
						handled = event.ctrlKey || event.metaKey;
						// -1 day on ctrl or command +left
						if (event.originalEvent.altKey) $.datepicker._adjustDate(event.target, (event.ctrlKey ?
								-$.datepicker._get(inst, 'stepBigMonths') :
								-$.datepicker._get(inst, 'stepMonths')), 'M');
						// next month/year on alt +left on Mac
						break;
					case 38: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, -7, 'D');
						handled = event.ctrlKey || event.metaKey;
						break; // -1 week on ctrl or command +up
					case 39: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, (isRTL ? -1 : +1), 'D');
						handled = event.ctrlKey || event.metaKey;
						// +1 day on ctrl or command +right
						if (event.originalEvent.altKey) $.datepicker._adjustDate(event.target, (event.ctrlKey ?
								+$.datepicker._get(inst, 'stepBigMonths') :
								+$.datepicker._get(inst, 'stepMonths')), 'M');
						// next month/year on alt +right
						break;
					case 40: if (event.ctrlKey || event.metaKey) $.datepicker._adjustDate(event.target, +7, 'D');
						handled = event.ctrlKey || event.metaKey;
						break; // +1 week on ctrl or command +down
					default: handled = false;
				}
			else if (event.keyCode == 36 && event.ctrlKey) // display the date picker on ctrl+home
				$.datepicker._showDatepicker(this);
			else {
				handled = false;
			}
			if (handled) {
				event.preventDefault();
				event.stopPropagation();
			}
		},

		/* Filter entered characters - based on date format. */
		_doKeyPress: function(event) {
			var inst = $.datepicker._getInst(event.target);
			if ($.datepicker._get(inst, 'constrainInput')) {
				var chars = $.datepicker._possibleChars($.datepicker._get(inst, 'dateFormat'));
				var chr = String.fromCharCode(event.charCode == undefined ? event.keyCode : event.charCode);
				return event.ctrlKey || event.metaKey || (chr < ' ' || !chars || chars.indexOf(chr) > -1);
			}
		},

		/* Synchronise manual entry and field/alternate field.
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 * erek
		 *
		 *
		 *
		 *
		 *
		 *
		 *
		 *************************************
		 */
		_doKeyUp: function(event) {
			//var inst = $.datepicker._getInst(event.target);
			//if (inst.input.val() != inst.lastVal) {
			//	try {
			//		var date = $.datepicker.parseDate($.datepicker._get(inst, 'dateFormat'),
			//			(inst.input ? inst.input.val() : null),
			//			$.datepicker._getFormatConfig(inst));
			//		if (date) { // only if valid
			//			$.datepicker._setDateFromField(inst);
			//			$.datepicker._updateAlternate(inst);
			//			$.datepicker._updateDatepicker(inst);
			//		}
			//	}
			//	catch (event) {
			//		$.datepicker.log(event);
			//	}
			//}
			//return true;
		},

		/* Pop-up the date picker for a given input field.
		 @param  input  element - the input field attached to the date picker or
		 event - if triggered by focus */
		_showDatepicker: function(input) {
			input = input.target || input;
			if (input.nodeName.toLowerCase() != 'input') // find from button/image trigger
				input = $('input', input.parentNode)[0];
			if ($.datepicker._isDisabledDatepicker(input) || $.datepicker._lastInput == input) // already here
				return;
			var inst = $.datepicker._getInst(input);
			if ($.datepicker._curInst && $.datepicker._curInst != inst) {
				$.datepicker._curInst.dpDiv.stop(true, true);
			}
			var beforeShow = $.datepicker._get(inst, 'beforeShow');
			extendRemove(inst.settings, (beforeShow ? beforeShow.apply(input, [input, inst]) : {}));
			inst.lastVal = null;
			$.datepicker._lastInput = input;
			$.datepicker._setDateFromField(inst);
			if ($.datepicker._inDialog) // hide cursor
				input.value = '';
			if (!$.datepicker._pos) { // position below input
				$.datepicker._pos = $.datepicker._findPos(input);
				$.datepicker._pos[1] += input.offsetHeight; // add the height
			}
			var isFixed = false;
			$(input).parents().each(function() {
				isFixed |= $(this).css('position') == 'fixed';
				return !isFixed;
			});
			if (isFixed && $.browser.opera) { // correction for Opera when fixed and scrolled
				$.datepicker._pos[0] -= document.documentElement.scrollLeft;
				$.datepicker._pos[1] -= document.documentElement.scrollTop;
			}
			var offset = {left: $.datepicker._pos[0], top: $.datepicker._pos[1]};
			$.datepicker._pos = null;
			//to avoid flashes on Firefox
			inst.dpDiv.empty();
			// determine sizing offscreen
			inst.dpDiv.css({position: 'absolute', display: 'block', top: '-1000px'});
			$.datepicker._updateDatepicker(inst);
			// fix width for dynamic number of date pickers
			// and adjust position before showing
			offset = $.datepicker._checkOffset(inst, offset, isFixed);
			inst.dpDiv.css({position: ($.datepicker._inDialog && $.blockUI ?
					'static' : (isFixed ? 'fixed' : 'absolute')), display: 'none',
				left: offset.left + 'px', top: offset.top + 'px'});
			if (!inst.inline) {
				var showAnim = $.datepicker._get(inst, 'showAnim');
				var duration = $.datepicker._get(inst, 'duration');
				var postProcess = function() {
					$.datepicker._datepickerShowing = true;
					var cover = inst.dpDiv.find('iframe.ui-datepicker-cover'); // IE6- only
					if( !! cover.length ){
						var borders = $.datepicker._getBorders(inst.dpDiv);
						cover.css({left: -borders[0], top: -borders[1],
							width: inst.dpDiv.outerWidth(), height: inst.dpDiv.outerHeight()});
					}
				};
				inst.dpDiv.zIndex($(input).zIndex()+1);
				if ($.effects && $.effects[showAnim])
					inst.dpDiv.show(showAnim, $.datepicker._get(inst, 'showOptions'), duration, postProcess);
				else
					inst.dpDiv[showAnim || 'show']((showAnim ? duration : null), postProcess);
				if (!showAnim || !duration)
					postProcess();
				if (inst.input.is(':visible') && !inst.input.is(':disabled'))
					inst.input.focus();
				$.datepicker._curInst = inst;
			}
		},

		/* Generate the date picker content. */
		_updateDatepicker: function(inst) {
			var self = this;
			var borders = $.datepicker._getBorders(inst.dpDiv);
			inst.dpDiv.empty().append(this._generateHTML(inst));
			var cover = inst.dpDiv.find('iframe.ui-datepicker-cover'); // IE6- only
			if( !!cover.length ){ //avoid call to outerXXXX() when not in IE6
				cover.css({left: -borders[0], top: -borders[1], width: inst.dpDiv.outerWidth(), height: inst.dpDiv.outerHeight()})
			}
			inst.dpDiv.find('button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a')
					.bind('mouseout', function(){
						$(this).removeClass('ui-state-hover');
						if(this.className.indexOf('ui-datepicker-prev') != -1) $(this).removeClass('ui-datepicker-prev-hover');
						if(this.className.indexOf('ui-datepicker-next') != -1) $(this).removeClass('ui-datepicker-next-hover');
					})
					.bind('mouseover', function(){
						if (!self._isDisabledDatepicker( inst.inline ? inst.dpDiv.parent()[0] : inst.input[0])) {
							$(this).parents('.ui-datepicker-calendar').find('a').removeClass('ui-state-hover');
							$(this).addClass('ui-state-hover');
							if(this.className.indexOf('ui-datepicker-prev') != -1) $(this).addClass('ui-datepicker-prev-hover');
							if(this.className.indexOf('ui-datepicker-next') != -1) $(this).addClass('ui-datepicker-next-hover');
						}
					})
					.end()
					.find('.' + this._dayOverClass + ' a')
					.trigger('mouseover')
					.end();
			var numMonths = this._getNumberOfMonths(inst);
			var cols = numMonths[1];
			var width = 17;
			if (cols > 1)
				inst.dpDiv.addClass('ui-datepicker-multi-' + cols).css('width', (width * cols) + 'em');
			else
				inst.dpDiv.removeClass('ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4').width('');
			inst.dpDiv[(numMonths[0] != 1 || numMonths[1] != 1 ? 'add' : 'remove') +
			'Class']('ui-datepicker-multi');
			inst.dpDiv[(this._get(inst, 'isRTL') ? 'add' : 'remove') +
			'Class']('ui-datepicker-rtl');
			if (inst == $.datepicker._curInst && $.datepicker._datepickerShowing && inst.input &&
						// #6694 - don't focus the input if it's already focused
						// this breaks the change event in IE
					inst.input.is(':visible') && !inst.input.is(':disabled') && inst.input[0] != document.activeElement)
				inst.input.focus();
			// deffered render of the years select (to avoid flashes on Firefox)
			if( inst.yearshtml ){
				var origyearshtml = inst.yearshtml;
				setTimeout(function(){
					//assure that inst.yearshtml didn't change.
					if( origyearshtml === inst.yearshtml ){
						inst.dpDiv.find('select.ui-datepicker-year:first').replaceWith(inst.yearshtml);
					}
					origyearshtml = inst.yearshtml = null;
				}, 0);
			}

			$(".ui-datepicker-calendar").find("a").each(function(){
				var action = $(this).attr("action");

				if($targetVal.indexOf(action) >= 0 && !$disableVals.indexOf(action) >= 0){
					$(this).css({background:"#62AFE7"});
					$(this).data("picker",{clicked:true});
				}
			});
		},

		/* Retrieve the size of left and top borders for an element.
		 @param  elem  (jQuery object) the element of interest
		 @return  (number[2]) the left and top borders */
		_getBorders: function(elem) {
			var convert = function(value) {
				return {thin: 1, medium: 2, thick: 3}[value] || value;
			};
			return [parseFloat(convert(elem.css('border-left-width'))),
				parseFloat(convert(elem.css('border-top-width')))];
		},

		/* Check positioning to remain on screen. */
		_checkOffset: function(inst, offset, isFixed) {
			var dpWidth = inst.dpDiv.outerWidth();
			var dpHeight = inst.dpDiv.outerHeight();
			var inputWidth = inst.input ? inst.input.outerWidth() : 0;
			var inputHeight = inst.input ? inst.input.outerHeight() : 0;
			var viewWidth = document.documentElement.clientWidth + $(document).scrollLeft();
			var viewHeight = document.documentElement.clientHeight + $(document).scrollTop();

			offset.left -= (this._get(inst, 'isRTL') ? (dpWidth - inputWidth) : 0);
			offset.left -= (isFixed && offset.left == inst.input.offset().left) ? $(document).scrollLeft() : 0;
			offset.top -= (isFixed && offset.top == (inst.input.offset().top + inputHeight)) ? $(document).scrollTop() : 0;

			// now check if datepicker is showing outside window viewport - move to a better place if so.
			offset.left -= Math.min(offset.left, (offset.left + dpWidth > viewWidth && viewWidth > dpWidth) ?
					Math.abs(offset.left + dpWidth - viewWidth) : 0);
			offset.top -= Math.min(offset.top, (offset.top + dpHeight > viewHeight && viewHeight > dpHeight) ?
					Math.abs(dpHeight + inputHeight) : 0);

			return offset;
		},

		/* Find an object's position on the screen. */
		_findPos: function(obj) {
			var inst = this._getInst(obj);
			var isRTL = this._get(inst, 'isRTL');
			while (obj && (obj.type == 'hidden' || obj.nodeType != 1 || $.expr.filters.hidden(obj))) {
				obj = obj[isRTL ? 'previousSibling' : 'nextSibling'];
			}
			var position = $(obj).offset();
			return [position.left, position.top];
		},

		/* Hide the date picker from view.d**
		 ***
		 ***
		 ***
		 ****
		 ***
		 **
		 ****
		 ***
		 ***
		 @param  input  element - the input field attached to the date picker */

		_hideDatepicker: function(input) {
			if(passCtrl){
				return;
			}
			var inst = this._curInst;
			if (!inst || (input && inst != $.data(input, PROP_NAME)))
				return;
			if (this._datepickerShowing) {
				var showAnim = this._get(inst, 'showAnim');
				var duration = this._get(inst, 'duration');
				var postProcess = function() {
					$.datepicker._tidyDialog(inst);
					this._curInst = null;
				};
				if ($.effects && $.effects[showAnim])
					inst.dpDiv.hide(showAnim, $.datepicker._get(inst, 'showOptions'), duration, postProcess);
				else{
					inst.dpDiv[(showAnim == 'slideDown' ? 'slideUp' :
							(showAnim == 'fadeIn' ? 'fadeOut' : 'hide'))]((showAnim ? duration : null), postProcess);
				}
				if (!showAnim)
					postProcess();
				var onClose = this._get(inst, 'onClose');
				if (onClose)
					onClose.apply((inst.input ? inst.input[0] : null),
							[(inst.input ? inst.input.val() : ''), inst]);  // trigger custom callback
				this._datepickerShowing = false;
				this._lastInput = null;
				if (this._inDialog) {
					this._dialogInput.css({ position: 'absolute', left: '0', top: '-100px' });
					if ($.blockUI) {
						$.unblockUI();
						$('body').append(this.dpDiv);
					}
				}
				this._inDialog = false;
			}
		},

		/* Tidy up after a dialog display. */
		_tidyDialog: function(inst) {
			inst.dpDiv.removeClass(this._dialogClass).unbind('.ui-datepicker-calendar');
		},

		/* Close date picker if clicked elsewhere. 点击空白处日期控件不会消失*/
		_checkExternalClick: function(event) {
			if (!$.datepicker._curInst)
				return;
			var $target = $(event.target);
			if ($target[0].id != $.datepicker._mainDivId &&
					$target.parents('#' + $.datepicker._mainDivId).length == 0 &&
					!$target.hasClass($.datepicker.markerClassName) &&
					!$target.hasClass($.datepicker._triggerClass) &&
					$.datepicker._datepickerShowing && !($.datepicker._inDialog && $.blockUI))
				$.datepicker._hideDatepicker();
		},

		/* Adjust one of the date sub-fields. */
		_adjustDate: function(id, offset, period) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			if (this._isDisabledDatepicker(target[0])) {
				return;
			}
			this._adjustInstDate(inst, offset +
					(period == 'M' ? this._get(inst, 'showCurrentAtPos') : 0), // undo positioning
					period);
			this._updateDatepicker(inst);
		},

		/* Action for current link. */
		_gotoToday: function(id) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
				inst.selectedDay = inst.currentDay;
				inst.drawMonth = inst.selectedMonth = inst.currentMonth;
				inst.drawYear = inst.selectedYear = inst.currentYear;
			}
			else {
				var date = new Date();
				inst.selectedDay = date.getDate();
				inst.drawMonth = inst.selectedMonth = date.getMonth();
				inst.drawYear = inst.selectedYear = date.getFullYear();
			}
			this._notifyChange(inst);
			this._adjustDate(target);
		},

		/* Action for selecting a new month/year. */
		_selectMonthYear: function(id, select, period) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			inst._selectingMonthYear = false;
			inst['selected' + (period == 'M' ? 'Month' : 'Year')] =
					inst['draw' + (period == 'M' ? 'Month' : 'Year')] =
							parseInt(select.options[select.selectedIndex].value,10);
			this._notifyChange(inst);
			this._adjustDate(target);
		},

		/* Restore input focus after not changing month/year. */
		_clickMonthYear: function(id) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			if (inst.input && inst._selectingMonthYear) {
				setTimeout(function() {
					inst.input.focus();
				}, 0);
			}
			inst._selectingMonthYear = !inst._selectingMonthYear;
		},

		/* Action for selecting a day. */
		_selectDay: function(id, month, year, td) {
			// alert("in");
			var target = $(id);
			if ($(td).hasClass(this._unselectableClass) || this._isDisabledDatepicker(target[0])) {
				return;
			}
			var inst = this._getInst(target[0]);
			inst.selectedDay = inst.currentDay = $('a', td).html();
			inst.selectedMonth = inst.currentMonth = month;
			inst.selectedYear = inst.currentYear = year;
			this._selectDate(id, this._formatDate(inst,
					inst.currentDay, inst.currentMonth, inst.currentYear));

			$(document).clicka(td);
			// alert(this._formatDate(inst,
			//      inst.currentDay, inst.currentMonth, inst.currentYear));
		},

		/* Erase the input field and hide the date picker. */
		_clearDate: function(id) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			this._selectDate(target, '');
		},

		/* Update the input field with the selected date. */
		_selectDate: function(id, dateStr) {
			var target = $(id);
			var inst = this._getInst(target[0]);
			dateStr = (dateStr != null ? dateStr : this._formatDate(inst));
			if (inst.input)
				inst.input.val(dateStr);
			this._updateAlternate(inst);
			var onSelect = this._get(inst, 'onSelect');
			if (onSelect)
				onSelect.apply((inst.input ? inst.input[0] : null), [dateStr, inst]);  // trigger custom callback
			else if (inst.input)
				inst.input.trigger('change'); // fire the change event
			if (inst.inline)
				this._updateDatepicker(inst);
			else {
				//this._hideDatepicker();
				this._lastInput = inst.input[0];
				if (typeof(inst.input[0]) != 'object')
					inst.input.focus(); // restore focus
				this._lastInput = null;
			}
		},

		/* Update any alternate field to synchronise with the main field. */
		_updateAlternate: function(inst) {
			var altField = this._get(inst, 'altField');
			if (altField) { // update alternate field too
				var altFormat = this._get(inst, 'altFormat') || this._get(inst, 'dateFormat');
				var date = this._getDate(inst);
				var dateStr = this.formatDate(altFormat, date, this._getFormatConfig(inst));
				$(altField).each(function() { $(this).val(dateStr); });
			}
		},

		/* Set as beforeShowDay function to prevent selection of weekends.
		 @param  date  Date - the date to customise
		 @return [boolean, string] - is this date selectable?, what is its CSS class? */
		noWeekends: function(date) {
			var day = date.getDay();
			return [(day > 0 && day < 6), ''];
		},

		/* Set as calculateWeek to determine the week of the year based on the ISO 8601 definition.
		 @param  date  Date - the date to get the week for
		 @return  number - the number of the week within the year that contains this date */
		iso8601Week: function(date) {
			var checkDate = new Date(date.getTime());
			// Find Thursday of this week starting on Monday
			checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7));
			var time = checkDate.getTime();
			checkDate.setMonth(0); // Compare with Jan 1
			checkDate.setDate(1);
			return Math.floor(Math.round((time - checkDate) / 86400000) / 7) + 1;
		},

		/* Parse a string value into a date object.
		 See formatDate below for the possible formats.

		 @param  format    string - the expected format of the date
		 @param  value     string - the date in the above format
		 @param  settings  Object - attributes include:
		 shortYearCutoff  number - the cutoff year for determining the century (optional)
		 dayNamesShort    string[7] - abbreviated names of the days from Sunday (optional)
		 dayNames         string[7] - names of the days from Sunday (optional)
		 monthNamesShort  string[12] - abbreviated names of the months (optional)
		 monthNames       string[12] - names of the months (optional)
		 @return  Date - the extracted date value or null if value is blank */
		parseDate: function (format, value, settings) {
			if (format == null || value == null)
				throw 'Invalid arguments';
			value = (typeof value == 'object' ? value.toString() : value + '');
			if (value == '')
				return null;
			var shortYearCutoff = (settings ? settings.shortYearCutoff : null) || this._defaults.shortYearCutoff;
			shortYearCutoff = (typeof shortYearCutoff != 'string' ? shortYearCutoff :
			new Date().getFullYear() % 100 + parseInt(shortYearCutoff, 10));
			var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
			var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
			var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
			var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
			var year = -1;
			var month = -1;
			var day = -1;
			var doy = -1;
			var literal = false;
			// Check whether a format character is doubled
			var lookAhead = function(match) {
				var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
				if (matches)
					iFormat++;
				return matches;
			};
			// Extract a number from the string value
			var getNumber = function(match) {
				var isDoubled = lookAhead(match);
				var size = (match == '@' ? 14 : (match == '!' ? 20 :
						(match == 'y' && isDoubled ? 4 : (match == 'o' ? 3 : 2))));
				var digits = new RegExp('^\\d{1,' + size + '}');
				var num = value.substring(iValue).match(digits);
				if (!num)
					throw 'Missing number at position ' + iValue;
				iValue += num[0].length;
				return parseInt(num[0], 10);
			};
			// Extract a name from the string value and convert to an index
			var getName = function(match, shortNames, longNames) {
				var names = (lookAhead(match) ? longNames : shortNames);
				for (var i = 0; i < names.length; i++) {
					if (value.substr(iValue, names[i].length).toLowerCase() == names[i].toLowerCase()) {
						iValue += names[i].length;
						return i + 1;
					}
				}
				throw 'Unknown name at position ' + iValue;
			};
			// Confirm that a literal character matches the string value
			var checkLiteral = function() {
				if (value.charAt(iValue) != format.charAt(iFormat))
					throw 'Unexpected literal at position ' + iValue;
				iValue++;
			};
			var iValue = 0;
			for (var iFormat = 0; iFormat < format.length; iFormat++) {
				if (literal)
					if (format.charAt(iFormat) == "'" && !lookAhead("'"))
						literal = false;
					else
						checkLiteral();
				else
					switch (format.charAt(iFormat)) {
						case 'd':
							day = getNumber('d');
							break;
						case 'D':
							getName('D', dayNamesShort, dayNames);
							break;
						case 'o':
							doy = getNumber('o');
							break;
						case 'm':
							month = getNumber('m');
							break;
						case 'M':
							month = getName('M', monthNamesShort, monthNames);
							break;
						case 'y':
							year = getNumber('y');
							break;
						case '@':
							var date = new Date(getNumber('@'));
							year = date.getFullYear();
							month = date.getMonth() + 1;
							day = date.getDate();
							break;
						case '!':
							var date = new Date((getNumber('!') - this._ticksTo1970) / 10000);
							year = date.getFullYear();
							month = date.getMonth() + 1;
							day = date.getDate();
							break;
						case "'":
							if (lookAhead("'"))
								checkLiteral();
							else
								literal = true;
							break;
						default:
							checkLiteral();
					}
			}
			if (year == -1)
				year = new Date().getFullYear();
			else if (year < 100)
				year += new Date().getFullYear() - new Date().getFullYear() % 100 +
						(year <= shortYearCutoff ? 0 : -100);
			if (doy > -1) {
				month = 1;
				day = doy;
				do {
					var dim = this._getDaysInMonth(year, month - 1);
					if (day <= dim)
						break;
					month++;
					day -= dim;
				} while (true);
			}
			var date = this._daylightSavingAdjust(new Date(year, month - 1, day));
			if (date.getFullYear() != year || date.getMonth() + 1 != month || date.getDate() != day)
				throw 'Invalid date'; // E.g. 31/02/*
			return date;
		},

		/* Standard date formats. */
		ATOM: 'yy-mm-dd', // RFC 3339 (ISO 8601)
		COOKIE: 'D, dd M yy',
		ISO_8601: 'yy-mm-dd',
		RFC_822: 'D, d M y',
		RFC_850: 'DD, dd-M-y',
		RFC_1036: 'D, d M y',
		RFC_1123: 'D, d M yy',
		RFC_2822: 'D, d M yy',
		RSS: 'D, d M y', // RFC 822
		TICKS: '!',
		TIMESTAMP: '@',
		W3C: 'yy-mm-dd', // ISO 8601

		_ticksTo1970: (((1970 - 1) * 365 + Math.floor(1970 / 4) - Math.floor(1970 / 100) +
		Math.floor(1970 / 400)) * 24 * 60 * 60 * 10000000),

		/* Format a date object into a string value.
		 The format can be combinations of the following:
		 d  - day of month (no leading zero)
		 dd - day of month (two digit)
		 o  - day of year (no leading zeros)
		 oo - day of year (three digit)
		 D  - day name short
		 DD - day name long
		 m  - month of year (no leading zero)
		 mm - month of year (two digit)
		 M  - month name short
		 MM - month name long
		 y  - year (two digit)
		 yy - year (four digit)
		 @ - Unix timestamp (ms since 01/01/1970)
		 ! - Windows ticks (100ns since 01/01/0001)
		 '...' - literal text
		 '' - single quote

		 @param  format    string - the desired format of the date
		 @param  date      Date - the date value to format
		 @param  settings  Object - attributes include:
		 dayNamesShort    string[7] - abbreviated names of the days from Sunday (optional)
		 dayNames         string[7] - names of the days from Sunday (optional)
		 monthNamesShort  string[12] - abbreviated names of the months (optional)
		 monthNames       string[12] - names of the months (optional)
		 @return  string - the date in the above format */
		formatDate: function (format, date, settings) {
			if (!date)
				return '';
			var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
			var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
			var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
			var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
			// Check whether a format character is doubled
			var lookAhead = function(match) {
				var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
				if (matches)
					iFormat++;
				return matches;
			};
			// Format a number, with leading zero if necessary
			var formatNumber = function(match, value, len) {
				var num = '' + value;
				if (lookAhead(match))
					while (num.length < len)
						num = '0' + num;
				return num;
			};
			// Format a name, short or long as requested
			var formatName = function(match, value, shortNames, longNames) {
				return (lookAhead(match) ? longNames[value] : shortNames[value]);
			};
			var output = '';
			var literal = false;
			if (date)
				for (var iFormat = 0; iFormat < format.length; iFormat++) {
					if (literal)
						if (format.charAt(iFormat) == "'" && !lookAhead("'"))
							literal = false;
						else
							output += format.charAt(iFormat);
					else
						switch (format.charAt(iFormat)) {
							case 'd':
								output += formatNumber('d', date.getDate(), 2);
								break;
							case 'D':
								output += formatName('D', date.getDay(), dayNamesShort, dayNames);
								break;
							case 'o':
								output += formatNumber('o',
										(date.getTime() - new Date(date.getFullYear(), 0, 0).getTime()) / 86400000, 3);
								break;
							case 'm':
								output += formatNumber('m', date.getMonth() + 1, 2);
								break;
							case 'M':
								output += formatName('M', date.getMonth(), monthNamesShort, monthNames);
								break;
							case 'y':
								output += (lookAhead('y') ? date.getFullYear() :
								(date.getYear() % 100 < 10 ? '0' : '') + date.getYear() % 100);
								break;
							case '@':
								output += date.getTime();
								break;
							case '!':
								output += date.getTime() * 10000 + this._ticksTo1970;
								break;
							case "'":
								if (lookAhead("'"))
									output += "'";
								else
									literal = true;
								break;
							default:
								output += format.charAt(iFormat);
						}
				}
			return output;
		},

		/* Extract all possible characters from the date format. */
		_possibleChars: function (format) {
			var chars = '';
			var literal = false;
			// Check whether a format character is doubled
			var lookAhead = function(match) {
				var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
				if (matches)
					iFormat++;
				return matches;
			};
			for (var iFormat = 0; iFormat < format.length; iFormat++)
				if (literal)
					if (format.charAt(iFormat) == "'" && !lookAhead("'"))
						literal = false;
					else
						chars += format.charAt(iFormat);
				else
					switch (format.charAt(iFormat)) {
						case 'd': case 'm': case 'y': case '@':
						chars += '0123456789';
						break;
						case 'D': case 'M':
						return null; // Accept anything
						case "'":
							if (lookAhead("'"))
								chars += "'";
							else
								literal = true;
							break;
						default:
							chars += format.charAt(iFormat);
					}
			return chars;
		},

		/* Get a setting value, defaulting if necessary. */
		_get: function(inst, name) {
			return inst.settings[name] !== undefined ?
					inst.settings[name] : this._defaults[name];
		},

		/* Parse existing date and initialise date picker. */
		_setDateFromField: function(inst, noDefault) {
			if (inst.input.val() == inst.lastVal) {
				return;
			}
			var dateFormat = this._get(inst, 'dateFormat');
			var dates = inst.lastVal = inst.input ? inst.input.val() : null;
			var date, defaultDate;
			date = defaultDate = this._getDefaultDate(inst);
			var settings = this._getFormatConfig(inst);
			try {
				date = this.parseDate(dateFormat, dates, settings) || defaultDate;
			} catch (event) {
				this.log(event);
				dates = (noDefault ? '' : dates);
			}
			inst.selectedDay = date.getDate();
			inst.drawMonth = inst.selectedMonth = date.getMonth();
			inst.drawYear = inst.selectedYear = date.getFullYear();
			inst.currentDay = (dates ? date.getDate() : 0);
			inst.currentMonth = (dates ? date.getMonth() : 0);
			inst.currentYear = (dates ? date.getFullYear() : 0);
			this._adjustInstDate(inst);
		},

		/* Retrieve the default date shown on opening. */
		_getDefaultDate: function(inst) {
			return this._restrictMinMax(inst,
					this._determineDate(inst, this._get(inst, 'defaultDate'), new Date()));
		},

		/* A date may be specified as an exact value or a relative one. */
		_determineDate: function(inst, date, defaultDate) {
			var offsetNumeric = function(offset) {
				var date = new Date();
				date.setDate(date.getDate() + offset);
				return date;
			};
			var offsetString = function(offset) {
				try {
					return $.datepicker.parseDate($.datepicker._get(inst, 'dateFormat'),
							offset, $.datepicker._getFormatConfig(inst));
				}
				catch (e) {
					// Ignore
				}
				var date = (offset.toLowerCase().match(/^c/) ?
								$.datepicker._getDate(inst) : null) || new Date();
				var year = date.getFullYear();
				var month = date.getMonth();
				var day = date.getDate();
				var pattern = /([+-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g;
				var matches = pattern.exec(offset);
				while (matches) {
					switch (matches[2] || 'd') {
						case 'd' : case 'D' :
						day += parseInt(matches[1],10); break;
						case 'w' : case 'W' :
						day += parseInt(matches[1],10) * 7; break;
						case 'm' : case 'M' :
						month += parseInt(matches[1],10);
						day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
						break;
						case 'y': case 'Y' :
						year += parseInt(matches[1],10);
						day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
						break;
					}
					matches = pattern.exec(offset);
				}
				return new Date(year, month, day);
			};
			var newDate = (date == null || date === '' ? defaultDate : (typeof date == 'string' ? offsetString(date) :
					(typeof date == 'number' ? (isNaN(date) ? defaultDate : offsetNumeric(date)) : new Date(date.getTime()))));
			newDate = (newDate && newDate.toString() == 'Invalid Date' ? defaultDate : newDate);
			if (newDate) {
				newDate.setHours(0);
				newDate.setMinutes(0);
				newDate.setSeconds(0);
				newDate.setMilliseconds(0);
			}
			return this._daylightSavingAdjust(newDate);
		},

		/* Handle switch to/from daylight saving.
		 Hours may be non-zero on daylight saving cut-over:
		 > 12 when midnight changeover, but then cannot generate
		 midnight datetime, so jump to 1AM, otherwise reset.
		 @param  date  (Date) the date to check
		 @return  (Date) the corrected date */
		_daylightSavingAdjust: function(date) {
			if (!date) return null;
			date.setHours(date.getHours() > 12 ? date.getHours() + 2 : 0);
			return date;
		},

		/* Set the date(s) directly. */
		_setDate: function(inst, date, noChange) {
			var clear = !date;
			var origMonth = inst.selectedMonth;
			var origYear = inst.selectedYear;
			var newDate = this._restrictMinMax(inst, this._determineDate(inst, date, new Date()));
			inst.selectedDay = inst.currentDay = newDate.getDate();
			inst.drawMonth = inst.selectedMonth = inst.currentMonth = newDate.getMonth();
			inst.drawYear = inst.selectedYear = inst.currentYear = newDate.getFullYear();
			if ((origMonth != inst.selectedMonth || origYear != inst.selectedYear) && !noChange)
				this._notifyChange(inst);
			this._adjustInstDate(inst);
			if (inst.input) {
				inst.input.val(clear ? '' : this._formatDate(inst));
			}
		},

		/* Retrieve the date(s) directly. */
		_getDate: function(inst) {
			var startDate = (!inst.currentYear || (inst.input && inst.input.val() == '') ? null :
					this._daylightSavingAdjust(new Date(
							inst.currentYear, inst.currentMonth, inst.currentDay)));
			return startDate;
		},

		/* Generate the HTML for the current state of the date picker. */
		_generateHTML: function(inst) {
			var today = new Date();
			today = this._daylightSavingAdjust(
					new Date(today.getFullYear(), today.getMonth(), today.getDate())); // clear time
			var isRTL = this._get(inst, 'isRTL');
			var showButtonPanel = this._get(inst, 'showButtonPanel');
			var hideIfNoPrevNext = this._get(inst, 'hideIfNoPrevNext');
			var navigationAsDateFormat = this._get(inst, 'navigationAsDateFormat');
			var numMonths = this._getNumberOfMonths(inst);
			var showCurrentAtPos = this._get(inst, 'showCurrentAtPos');
			var stepMonths = this._get(inst, 'stepMonths');
			var isMultiMonth = (numMonths[0] != 1 || numMonths[1] != 1);
			var currentDate = this._daylightSavingAdjust((!inst.currentDay ? new Date(9999, 9, 9) :
					new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
			var minDate = this._getMinMaxDate(inst, 'min');
			var maxDate = this._getMinMaxDate(inst, 'max');
			var drawMonth = inst.drawMonth - showCurrentAtPos;
			var drawYear = inst.drawYear;
			if (drawMonth < 0) {
				drawMonth += 12;
				drawYear--;
			}
			if (maxDate) {
				var maxDraw = this._daylightSavingAdjust(new Date(maxDate.getFullYear(),
						maxDate.getMonth() - (numMonths[0] * numMonths[1]) + 1, maxDate.getDate()));
				maxDraw = (minDate && maxDraw < minDate ? minDate : maxDraw);
				while (this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1)) > maxDraw) {
					drawMonth--;
					if (drawMonth < 0) {
						drawMonth = 11;
						drawYear--;
					}
				}
			}
			inst.drawMonth = drawMonth;
			inst.drawYear = drawYear;
			var prevText = this._get(inst, 'prevText');
			prevText = (!navigationAsDateFormat ? prevText : this.formatDate(prevText,
					this._daylightSavingAdjust(new Date(drawYear, drawMonth - stepMonths, 1)),
					this._getFormatConfig(inst)));
			var prev = (this._canAdjustMonth(inst, -1, drawYear, drawMonth) ?
			'<a class="ui-datepicker-prev ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._adjustDate(\'#' + inst.id + '\', -' + stepMonths + ', \'M\');"' +
			' title="' + prevText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'e' : 'w') + '">' + prevText + '</span></a>' :
					(hideIfNoPrevNext ? '' : '<a class="ui-datepicker-prev ui-corner-all ui-state-disabled" title="'+ prevText +'"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'e' : 'w') + '">' + prevText + '</span></a>'));
			var nextText = this._get(inst, 'nextText');
			nextText = (!navigationAsDateFormat ? nextText : this.formatDate(nextText,
					this._daylightSavingAdjust(new Date(drawYear, drawMonth + stepMonths, 1)),
					this._getFormatConfig(inst)));
			var next = (this._canAdjustMonth(inst, +1, drawYear, drawMonth) ?
			'<a class="ui-datepicker-next ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._adjustDate(\'#' + inst.id + '\', +' + stepMonths + ', \'M\');"' +
			' title="' + nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'w' : 'e') + '">' + nextText + '</span></a>' :
					(hideIfNoPrevNext ? '' : '<a class="ui-datepicker-next ui-corner-all ui-state-disabled" title="'+ nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + ( isRTL ? 'w' : 'e') + '">' + nextText + '</span></a>'));
			var currentText = this._get(inst, 'currentText');
			var gotoDate = (this._get(inst, 'gotoCurrent') && inst.currentDay ? currentDate : today);
			currentText = (!navigationAsDateFormat ? currentText :
					this.formatDate(currentText, gotoDate, this._getFormatConfig(inst)));
			var controls = (!inst.inline ? '<button type="button" class="ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._hideDatepicker();">' + this._get(inst, 'closeText') + '</button>' : '');
			var buttonPanel = (showButtonPanel) ? '<div class="ui-datepicker-buttonpane ui-widget-content">' + (isRTL ? controls : '') +
			(this._isInRange(inst, gotoDate) ? '<button type="button" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" onclick="DP_jQuery_' + dpuuid +
			'.datepicker._gotoToday(\'#' + inst.id + '\');"' +
			'>' + currentText + '</button>' : '') + (isRTL ? '' : controls) + '</div>' : '';
			var firstDay = parseInt(this._get(inst, 'firstDay'),10);
			firstDay = (isNaN(firstDay) ? 0 : firstDay);
			var showWeek = this._get(inst, 'showWeek');
			var dayNames = this._get(inst, 'dayNames');
			var dayNamesShort = this._get(inst, 'dayNamesShort');
			var dayNamesMin = this._get(inst, 'dayNamesMin');
			var monthNames = this._get(inst, 'monthNames');
			var monthNamesShort = this._get(inst, 'monthNamesShort');
			var beforeShowDay = this._get(inst, 'beforeShowDay');
			var showOtherMonths = this._get(inst, 'showOtherMonths');
			var selectOtherMonths = this._get(inst, 'selectOtherMonths');
			var calculateWeek = this._get(inst, 'calculateWeek') || this.iso8601Week;
			var defaultDate = this._getDefaultDate(inst);
			var html = '';
			for (var row = 0; row < numMonths[0]; row++) {
				var group = '';
				for (var col = 0; col < numMonths[1]; col++) {
					var selectedDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, inst.selectedDay));
					var cornerClass = ' ui-corner-all';
					var calender = '';
					if (isMultiMonth) {
						calender += '<div class="ui-datepicker-group';
						if (numMonths[1] > 1)
							switch (col) {
								case 0: calender += ' ui-datepicker-group-first';
									cornerClass = ' ui-corner-' + (isRTL ? 'right' : 'left'); break;
								case numMonths[1]-1: calender += ' ui-datepicker-group-last';
									cornerClass = ' ui-corner-' + (isRTL ? 'left' : 'right'); break;
								default: calender += ' ui-datepicker-group-middle'; cornerClass = ''; break;
							}
						calender += '">';
					}
					calender += '<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix' + cornerClass + '">' +
							(/all|left/.test(cornerClass) && row == 0 ? (isRTL ? next : prev) : '') +
							(/all|right/.test(cornerClass) && row == 0 ? (isRTL ? prev : next) : '') +
							this._generateMonthYearHeader(inst, drawMonth, drawYear, minDate, maxDate,
									row > 0 || col > 0, monthNames, monthNamesShort) + // draw month headers
							'</div><table class="ui-datepicker-calendar"><thead>' +
							'<tr>';
					var thead = (showWeek ? '<th class="ui-datepicker-week-col">' + this._get(inst, 'weekHeader') + '</th>' : '');
					for (var dow = 0; dow < 7; dow++) { // days of the week
						var day = (dow + firstDay) % 7;
						thead += '<th' + ((dow + firstDay + 6) % 7 >= 5 ? ' class="ui-datepicker-week-end"' : '') + '>' +
								'<span title="' + dayNames[day] + '">' + dayNamesMin[day] + '</span></th>';
					}
					calender += thead + '</tr></thead><tbody>';
					var daysInMonth = this._getDaysInMonth(drawYear, drawMonth);
					if (drawYear == inst.selectedYear && drawMonth == inst.selectedMonth)
						inst.selectedDay = Math.min(inst.selectedDay, daysInMonth);
					var leadDays = (this._getFirstDayOfMonth(drawYear, drawMonth) - firstDay + 7) % 7;
					var numRows = (isMultiMonth ? 6 : Math.ceil((leadDays + daysInMonth) / 7)); // calculate the number of rows to generate
					var printDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1 - leadDays));
					for (var dRow = 0; dRow < numRows; dRow++) { // create date picker rows
						calender += '<tr>';
						var tbody = (!showWeek ? '' : '<td class="ui-datepicker-week-col">' +
						this._get(inst, 'calculateWeek')(printDate) + '</td>');
						for (var dow = 0; dow < 7; dow++) { // create date picker days
							var daySettings = (beforeShowDay ?
									beforeShowDay.apply((inst.input ? inst.input[0] : null), [printDate]) : [true, '']);
							var otherMonth = (printDate.getMonth() != drawMonth);
							var unselectable = (otherMonth && !selectOtherMonths) || !daySettings[0] ||
									(minDate && printDate < minDate) || (maxDate && printDate > maxDate);

							var disablearrays = $disableVals.split(",");
							var len = disablearrays.length;
							for(var k=0;k<len;k++){
								var datestr = disablearrays[k];
								if(datestr!=""){
									var dates = datestr.split("-");
									var year = dates[0];
									var month = dates[1];
									var day = dates[2];
									var disabledate = this._daylightSavingAdjust(new Date(year, month-1, day));
									if(printDate.getTime()==disabledate.getTime()){
										//alert(disabledate);
										unselectable = true;
										break;
									}
								}
							}
							var _selectedDate = this._formatDate(inst,printDate.getDate(),printDate.getMonth(),printDate.getFullYear());

							var groupPriceFlag = $("#groupPriceFlag").val();
							var groupPriceFun = "";
							if (groupPriceFlag == "true") {
								groupPriceFun = "copyGroupPrice();";
							}

							tbody += '<td class="' +
									((dow + firstDay + 6) % 7 >= 5 ? ' ui-datepicker-week-end' : '') + // highlight weekends
									(otherMonth ? ' ui-datepicker-other-month' : '') + // highlight days from other months
									((printDate.getTime() == selectedDate.getTime() && drawMonth == inst.selectedMonth && inst._keyEvent) || // user pressed key
									(defaultDate.getTime() == printDate.getTime() && defaultDate.getTime() == selectedDate.getTime()) ?
										// or defaultDate is current printedDate and defaultDate is selectedDate
									' ' + this._dayOverClass : '') + // highlight selected day
									(unselectable ? ' ' + this._unselectableClass + ' ui-state-disabled': '') +  // highlight unselectable days
									(otherMonth && !showOtherMonths ? '' : ' ' + daySettings[1] + // highlight custom dates
									(printDate.getTime() == currentDate.getTime() ? ' ' + this._currentClass : '') + // highlight selected day
									(printDate.getTime() == today.getTime() ? ' ui-datepicker-today' : '')) + '"' + // highlight today (if different)
									((!otherMonth || showOtherMonths) && daySettings[2] ? ' title="' + daySettings[2] + '"' : '') + // cell title
									(unselectable ? '' : ' onclick="DP_jQuery_' + dpuuid + '.datepicker._selectDay(\'#' +
									inst.id + '\',' + printDate.getMonth() + ',' + printDate.getFullYear() + ', this);' + groupPriceFun + 'return false;"') + '>' + // actions
									(otherMonth && !showOtherMonths ? '&#xa0;' : // display for other months

											(unselectable && $disableVals.match(_selectedDate)? '<span style="background-color:#808080">' + printDate.getDate() + '</span>' : unselectable ? '<span class="ui-state-default">' + printDate.getDate() + '</span>' : '<a class="ui-state-default' +
											(printDate.getTime() == today.getTime() ? ' ui-state-highlight' : '') +
											(printDate.getTime() == currentDate.getTime() ? ' ui-state-active' : '') + // highlight selected day
											(otherMonth ? ' ui-priority-secondary' : '') + // distinguish dates from other months
											'" href="#" action="'+
											this._formatDate(inst,printDate.getDate(),printDate.getMonth(),printDate.getFullYear()) +'">' + printDate.getDate() + '</a>')) + '</td>'; // display selectable date
							printDate.setDate(printDate.getDate() + 1);
							printDate = this._daylightSavingAdjust(printDate);
						}
						calender += tbody + '</tr>';
					}
					drawMonth++;
					if (drawMonth > 11) {
						drawMonth = 0;
						drawYear++;
					}
					calender += '</tbody></table>' + (isMultiMonth ? '</div>' +
							((numMonths[0] > 0 && col == numMonths[1]-1) ? '<div class="ui-datepicker-row-break"></div>' : '') : '');
					group += calender;
				}
				html += group;
			}
			html += buttonPanel + ($.browser.msie && parseInt($.browser.version,10) < 7 && !inst.inline ?
							'<iframe src="javascript:false;" class="ui-datepicker-cover" frameborder="0"></iframe>' : '');
			inst._keyEvent = false;
			return html;
		},

		/* Generate the month and year header. */
		_generateMonthYearHeader: function(inst, drawMonth, drawYear, minDate, maxDate,
										   secondary, monthNames, monthNamesShort) {
			var changeMonth = this._get(inst, 'changeMonth');
			var changeYear = this._get(inst, 'changeYear');
			var showMonthAfterYear = this._get(inst, 'showMonthAfterYear');
			var html = '<div class="ui-datepicker-title">';
			var monthHtml = '';
			// month selection
			if (secondary || !changeMonth)
				monthHtml += '<span class="ui-datepicker-month">' + monthNames[drawMonth] + '</span>';
			else {
				var inMinYear = (minDate && minDate.getFullYear() == drawYear);
				var inMaxYear = (maxDate && maxDate.getFullYear() == drawYear);
				monthHtml += '<select class="ui-datepicker-month" ' +
						'onchange="DP_jQuery_' + dpuuid + '.datepicker._selectMonthYear(\'#' + inst.id + '\', this, \'M\');" ' +
						'onclick="DP_jQuery_' + dpuuid + '.datepicker._clickMonthYear(\'#' + inst.id + '\');"' +
						'>';
				for (var month = 0; month < 12; month++) {
					if ((!inMinYear || month >= minDate.getMonth()) &&
							(!inMaxYear || month <= maxDate.getMonth()))
						monthHtml += '<option value="' + month + '"' +
								(month == drawMonth ? ' selected="selected"' : '') +
								'>' + monthNamesShort[month] + '</option>';
				}
				monthHtml += '</select>';
			}
			if (!showMonthAfterYear)
				html += monthHtml + (secondary || !(changeMonth && changeYear) ? '&#xa0;' : '');
			// year selection
			inst.yearshtml = '';
			if (secondary || !changeYear)
				html += '<span class="ui-datepicker-year">' + drawYear + '</span>';
			else {
				// determine range of years to display
				var years = this._get(inst, 'yearRange').split(':');
				var thisYear = new Date().getFullYear();
				var determineYear = function(value) {
					var year = (value.match(/c[+-].*/) ? drawYear + parseInt(value.substring(1), 10) :
							(value.match(/[+-].*/) ? thisYear + parseInt(value, 10) :
									parseInt(value, 10)));
					return (isNaN(year) ? thisYear : year);
				};
				var year = determineYear(years[0]);
				var endYear = Math.max(year, determineYear(years[1] || ''));
				year = (minDate ? Math.max(year, minDate.getFullYear()) : year);
				endYear = (maxDate ? Math.min(endYear, maxDate.getFullYear()) : endYear);
				inst.yearshtml += '<select class="ui-datepicker-year" ' +
						'onchange="DP_jQuery_' + dpuuid + '.datepicker._selectMonthYear(\'#' + inst.id + '\', this, \'Y\');" ' +
						'onclick="DP_jQuery_' + dpuuid + '.datepicker._clickMonthYear(\'#' + inst.id + '\');"' +
						'>';
				for (; year <= endYear; year++) {
					inst.yearshtml += '<option value="' + year + '"' +
							(year == drawYear ? ' selected="selected"' : '') +
							'>' + year + '</option>';
				}
				inst.yearshtml += '</select>';
				//when showing there is no need for later update
				if( ! $.browser.mozilla ){
					html += inst.yearshtml;
					inst.yearshtml = null;
				} else {
					// will be replaced later with inst.yearshtml
					html += '<select class="ui-datepicker-year"><option value="' + drawYear + '" selected="selected">' + drawYear + '</option></select>';
				}
			}
			html += this._get(inst, 'yearSuffix');
			if (showMonthAfterYear)
				html += (secondary || !(changeMonth && changeYear) ? '&#xa0;' : '') + monthHtml;
			html += '</div>'; // Close datepicker_header
			return html;
		},

		/* Adjust one of the date sub-fields. */
		_adjustInstDate: function(inst, offset, period) {
			var year = inst.drawYear + (period == 'Y' ? offset : 0);
			var month = inst.drawMonth + (period == 'M' ? offset : 0);
			var day = Math.min(inst.selectedDay, this._getDaysInMonth(year, month)) +
					(period == 'D' ? offset : 0);
			var date = this._restrictMinMax(inst,
					this._daylightSavingAdjust(new Date(year, month, day)));
			inst.selectedDay = date.getDate();
			inst.drawMonth = inst.selectedMonth = date.getMonth();
			inst.drawYear = inst.selectedYear = date.getFullYear();
			if (period == 'M' || period == 'Y')
				this._notifyChange(inst);
		},

		/* Ensure a date is within any min/max bounds. */
		_restrictMinMax: function(inst, date) {
			var minDate = this._getMinMaxDate(inst, 'min');
			var maxDate = this._getMinMaxDate(inst, 'max');
			var newDate = (minDate && date < minDate ? minDate : date);
			newDate = (maxDate && newDate > maxDate ? maxDate : newDate);
			return newDate;
		},

		/* Notify change of month/year. */
		_notifyChange: function(inst) {
			var onChange = this._get(inst, 'onChangeMonthYear');
			if (onChange)
				onChange.apply((inst.input ? inst.input[0] : null),
						[inst.selectedYear, inst.selectedMonth + 1, inst]);
		},

		/* Determine the number of months to show. */
		_getNumberOfMonths: function(inst) {
			var numMonths = this._get(inst, 'numberOfMonths');
			return (numMonths == null ? [1, 1] : (typeof numMonths == 'number' ? [1, numMonths] : numMonths));
		},

		/* Determine the current maximum date - ensure no time components are set. */
		_getMinMaxDate: function(inst, minMax) {
			return this._determineDate(inst, this._get(inst, minMax + 'Date'), null);
		},

		/* Find the number of days in a given month. */
		_getDaysInMonth: function(year, month) {
			return 32 - this._daylightSavingAdjust(new Date(year, month, 32)).getDate();
		},

		/* Find the day of the week of the first of a month. */
		_getFirstDayOfMonth: function(year, month) {
			return new Date(year, month, 1).getDay();
		},

		/* Determines if we should allow a "next/prev" month display change. */
		_canAdjustMonth: function(inst, offset, curYear, curMonth) {
			var numMonths = this._getNumberOfMonths(inst);
			var date = this._daylightSavingAdjust(new Date(curYear,
					curMonth + (offset < 0 ? offset : numMonths[0] * numMonths[1]), 1));
			if (offset < 0)
				date.setDate(this._getDaysInMonth(date.getFullYear(), date.getMonth()));
			return this._isInRange(inst, date);
		},

		/* Is the given date in the accepted range? */
		_isInRange: function(inst, date) {
			var minDate = this._getMinMaxDate(inst, 'min');
			var maxDate = this._getMinMaxDate(inst, 'max');
			return ((!minDate || date.getTime() >= minDate.getTime()) &&
			(!maxDate || date.getTime() <= maxDate.getTime()));
		},

		/* Provide the configuration settings for formatting/parsing. */
		_getFormatConfig: function(inst) {
			var shortYearCutoff = this._get(inst, 'shortYearCutoff');
			shortYearCutoff = (typeof shortYearCutoff != 'string' ? shortYearCutoff :
			new Date().getFullYear() % 100 + parseInt(shortYearCutoff, 10));
			return {shortYearCutoff: shortYearCutoff,
				dayNamesShort: this._get(inst, 'dayNamesShort'), dayNames: this._get(inst, 'dayNames'),
				monthNamesShort: this._get(inst, 'monthNamesShort'), monthNames: this._get(inst, 'monthNames')};
		},

		/* Format the given date for display. */
		_formatDate: function(inst, day, month, year) {
			if (!day) {
				inst.currentDay = inst.selectedDay;
				inst.currentMonth = inst.selectedMonth;
				inst.currentYear = inst.selectedYear;
			}
			var date = (day ? (typeof day == 'object' ? day :
					this._daylightSavingAdjust(new Date(year, month, day))) :
					this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
			return this.formatDate(this._get(inst, 'dateFormat'), date, this._getFormatConfig(inst));
		}
	});

	/* jQuery extend now ignores nulls! */
	function extendRemove(target, props) {
		$.extend(target, props);
		for (var name in props)
			if (props[name] == null || props[name] == undefined)
				target[name] = props[name];
		return target;
	};

	/* Determine whether an object is an array. */
	function isArray(a) {
		return (a && (($.browser.safari && typeof a == 'object' && a.length) ||
		(a.constructor && a.constructor.toString().match(/\Array\(\)/))));
	};

	/* Invoke the datepicker functionality.
	 @param  options  string - a command, optionally followed by additional parameters or
	 Object - settings for attaching new datepicker functionality
	 @return  jQuery object */
	$.fn.datepicker = function(options){

		/* Verify an empty collection wasn't passed - Fixes #6976 */
		if ( !this.length ) {
			return this;
		}

		/* Initialise the date picker. */
		if (!$.datepicker.initialized) {
			$(document).mousedown($.datepicker._checkExternalClick).
			find('body').append($.datepicker.dpDiv);
			$.datepicker.initialized = true;
		}

		var otherArgs = Array.prototype.slice.call(arguments, 1);
		if (typeof options == 'string' && (options == 'isDisabled' || options == 'getDate' || options == 'widget'))
			return $.datepicker['_' + options + 'Datepicker'].
			apply($.datepicker, [this[0]].concat(otherArgs));
		if (options == 'option' && arguments.length == 2 && typeof arguments[1] == 'string')
			return $.datepicker['_' + options + 'Datepicker'].
			apply($.datepicker, [this[0]].concat(otherArgs));
		return this.each(function() {
			typeof options == 'string' ?
					$.datepicker['_' + options + 'Datepicker'].apply($.datepicker, [this].concat(otherArgs)) :
					$.datepicker._attachDatepicker(this, options);
		});
	};

	$.datepicker = new Datepicker(); // singleton instance
	$.datepicker.initialized = false;
	$.datepicker.uuid = new Date().getTime();
	$.datepicker.version = "1.8.10";

// Workaround for #4055
// Add another global to avoid noConflict issues with inline event handlers
	window['DP_jQuery_' + dpuuid] = $;

})(jQuery);


$(function(){
	$.fn.datepickerRefactor = function(options,dateStart,dateEnd,groupTable) { //datepickerRefactor
		var opts = $.extend({}, $.fn.datepickerRefactor.defaults,options);

		var $this = $(this);
		var $input = "<input id='datepicker_input' type='button' style='width:1px; height:1px; border:0px;background:transparent;'>";
		var passShift = false;

		var focusCss = {background:opts.focusColor};
		var blurCss = {background:opts.blurColor};

		//$input.appendTo($this);
		$this.append($input);
		$("#datepicker_input").datepicker(opts).change(function(){

			var willVal = $(this).val();
			var targetVal = $(opts.target).val();

			if(targetVal.indexOf(willVal) < 0){
				$(opts.target).val(targetVal + willVal + ",");
				//writeDate(willVal,index);
				//index++;
			}
			$targetVal = $(opts.target).val();
		});

		$targetVal = $(opts.target).val();

		var $a = $(".ui-datepicker-calendar").find("a");

		this.click(function(){
			$("#datepicker_input").focus();
		});


		$a.live("click",function(){
			var aa = $(this);
			$targetVal = $(opts.target).val();
			if(isClick(aa)){
				aa.css(blurCss);
				aa.data("picker",{clicked:false});
				delVal();
			}else{
				aa.css(focusCss);
				aa.data("picker",{clicked:true});
			}

		});

		/**
		 * 重写最早最晚出团日期
		 */
		function rewriteDate($targetVal,dateStart,dateEnd,groupTable){
			var datesArray = $targetVal.split(",");
			var maxDate;
			var minDate;
			if(datesArray.length>=2){
				maxDate = $(document).parseISO8601(datesArray[0]);
				minDate = $(document).parseISO8601(datesArray[0]);
				for(var i=0;i<datesArray.length-1;i++){
					var date=$(document).parseISO8601(datesArray[i]);
					if(date.getTime()>maxDate.getTime())
						maxDate = date;
					if(date.getTime()<minDate.getTime())
						minDate = date;
				}
				for(var i=0;i<0;i++){
					$("#groupCloseDate"+i).datepicker({
						dateFormat:"yy-mm-dd",
						dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
						closeText:"关闭",
						prevText:"前一月",
						nextText:"后一月",
						monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
					});
					$("#visaDate"+i).datepicker({
						dateFormat:"yy-mm-dd",
						dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
						closeText:"关闭",
						prevText:"前一月",
						nextText:"后一月",
						monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
					});
				}
				yy=maxDate.getFullYear();
				mm=$.trim(maxDate.getMonth()+1).length==1?"0"+(maxDate.getMonth()+1):maxDate.getMonth()+1;
				dd=$.trim(maxDate.getDate()).length==1?"0"+maxDate.getDate():maxDate.getDate();
				$(dateEnd).val(yy+'-'+mm+'-'+dd);
				yy=minDate.getFullYear();
				mm=$.trim(minDate.getMonth()+1).length==1?"0"+(minDate.getMonth()+1):minDate.getMonth()+1;
				dd=$.trim(minDate.getDate()).length==1?"0"+minDate.getDate():minDate.getDate();
				$(dateStart).val(yy+'-'+mm+'-'+dd);
			}else{
				//$(groupTable).empty();
				$(dateStart).val("");
				$(dateEnd).val("");
			}
		}

		$a.live("mousemove",function(){
			var aa = $(this);
			if(passShift){
				if(!isClick(aa)){
					$(this).click();
				}
			}

		});

		//是否按下SHIFT
		$(document).keydown(function(event){
			//alert(event.keyCode);
			if(event.keyCode == 16){
				passShift = true;
			}
		}).keyup(function(){
			passShift = false;
		});

		var groupNum = '';
		function getMaxCount(date) {
			//debugger;
			// c460
			if(0==groupCodeRuleDT){//groupCodeRuleDT:0手动  1自动
				groupNum = '';
			}else{//
				//新行者团号生成规则
				if(71 == $("#companyId").val()) {
					var groupOpenDate = "";
					if($("#groupCodeRule").children("option:selected").text() == 'BJ-SP') {
						groupOpenDate = date;
					}



					if($("#secondStepEnd").find("[name='groupCode']").length == 0) {
						$.ajax({
							type:"POST",
							async : false,
							url:$("#ctx").val() + "/activity/manager/getCurrentDateMaxGroupCode?groupOpenDate=" + groupOpenDate+"&tmp="+new Date().getTime(),
							success:function(result){

								//groupNum = result;
								//debugger;
								//解决bug   13128    与 c460 需求一同修改（改bug与本需求无关）
								var dateTemp = date.replace("-","").replace("-","");
								var groupnumSufix = result.split("-")[1];
								groupNum = dateTemp+"-"+groupnumSufix;
							}
						});
						//如果是新行者的规则再次添加团期，则需要在本地累加，不从数据库获取。
					}else{
						var codeDate = date.replace(/-/g,"");
						var countArr = groupNum.split("-");
						var count = Number(countArr[countArr.length - 1]) + 1;
						if(1 == count.toString().length){
							countArr[countArr.length - 1] = "000" + count;
						}else if(2 == count.toString().length){
							countArr[countArr.length - 1] = "00" + count;
						}else if(3 == count.toString().length){
							countArr[countArr.length - 1] = "0" + count;
						}else{
							countArr[countArr.length - 1] = count;
						}
						countArr[countArr.length - 2] = codeDate;
						groupNum = countArr.join("-");
					}
					return $("#groupCodeRule option:selected").text()+"-"+groupNum;

				}else if(68 == $("#companyId").val()){
					//如果是环球行的规则再次添加团期，则需要在本地累加，不从数据库获取。
//					if(1 <= $("#secondStepEnd").find("[name='groupCode']").length){
//						var countArr = groupNum.split("-");
//						//获取出团日期后4位
//						var groupDateArr = date.split("-");
//						var groupDateLast = groupDateArr[1] + groupDateArr[2];
//						countArr[countArr.length - 1] = groupDateLast;
//						var count = Number(countArr[countArr.length - 2]) + 1;
//						if(1 == count.toString().length){
//							countArr[countArr.length - 2] = "000" + count;
//						}else if(2 == count.toString().length){
//							countArr[countArr.length - 2] = "00" + count;
//						}else if(3 == count.toString().length) {
//							countArr[countArr.length - 2] = "0" + count;
//						}else{
//							countArr[countArr.length - 2] = count;
//						}
//						groupNum = countArr.join("-");
//					}else{
					var deptId = ("" == $("#treeDeptId").val() || $("#treeDeptId").val() == undefined) ? $("#deptId").val() : $("#treeDeptId").val();
					$.ajax({
						type:"POST",
						async : false,
						url:$("#ctx").val()+"/activity/manager/getGroupNum"+"?tmp="+new Date().getTime(),
						data:{
							deptId : deptId,
							groupOpenDate : date
						},
						success:function(result){
							groupNum = result;
						}
					});
					//}
				}else if($("#companyUUID").val()=='7a8177e377a811e5bc1e000c29cf2586'){
					//1:境外游,0:境内游
					if($("#deptCodeFlag").val()=='1'){
						var deptCode = $("#deptCode").val();
						$.ajax({
							type:"POST",
							async : false,
							url:$("#ctx").val()+"/activity/manager/getGroupNum",
							data:{
								deptId : deptCode,
								groupOpenDate : date
							},
							success:function(result){
								groupNum = result;
							}
						});
					}else{
						groupNum = '';
					}
					//大洋旅游单团，散拼手动输入团号
				}else if($("#companyUUID").val()=='7a81a03577a811e5bc1e000c29cf2586'
							//拉美图单团，散拼，大客户，自由行，游轮手动输入团号
						|| $("#companyUUID").val()=='7a81a26b77a811e5bc1e000c29cf2586'
							//大唐国旅(友创国际)团期类产品手动输入团号
						|| $("#companyUUID").val()=='7a45838277a811e5bc1e000c29cf2586'
							//诚品旅游团期类产品手动输入团号
						|| $("#companyUUID").val()=='ed88f3507ba0422b859e6d7e62161b00'
							//日信观光团期类产品手动输入团号
						|| $("#companyUUID").val()=='58a27feeab3944378b266aff05b627d2'
							//懿洋假期团期类产品手动输入团号
						|| $("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'
							//非常国际团期类产品手动输入团号
						|| $("#companyUUID").val()=='1d4462b514a84ee2893c551a355a82d2'
							//优加国际团期类产品手动输入团号
						|| $("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586'
							//起航假期团期类产品手动输入团号
						|| $("#companyUUID").val()=='5c05dfc65cd24c239cd1528e03965021'
							// 对应需求号    c460
						|| (groupCodeRuleDT == 0)){
					groupNum = '';
					//北京名扬国际旅行社
				}else if($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586'){
					var groupNoMark = $("#groupNoMark").val();
					$.ajax({
						type:"POST",
						async : false,
						url:$("#ctx").val()+"/activity/manager/getGroupNum",
						data:{
							deptId : groupNoMark,
							groupOpenDate : date
						},
						success:function(result){
							groupNum = result;
						}
					});
				}else{
					$.ajax({
						type:"POST",
						async : false,
						url:$("#ctx").val()+"/activity/manager/getGroupNum",
						success:function(result){
							groupNum = result;
						}
					});
				}
			}
			return groupNum;
		}

		function getTrHtml(){
			var closeBeforeDays = opts.closeBeforeDays;
			var visaBeforeDays = opts.visaBeforeDays;
			$targetVal = $(opts.target).val();
			if($targetVal!=""){
				$(closeBeforeDays).attr("disabled",false);
				$(visaBeforeDays).attr("disabled",false);
				$(opts.visaCountryCopy).attr("disabled",false);
				$(opts.visaCopyBtn).attr("disabled",false);
				$(opts.planPositionCopy).attr("disabled",false);
				$(opts.planPositionBtn).attr("disabled",false);
				$(opts.freePositionCopy).attr("disabled",false);
				$(opts.freePositionBtn).attr("disabled",false);
				var empty = $("#contentTable tbody:eq(0)").find("tr[id='emptygroup']");
				if(empty.length!=0)
					$(empty[0]).remove();
				var tbody = $("#contentTable tbody:eq(0)");
				var dates = $targetVal.split(",");
				$(dates).each(function(i,value){
					var date = value.toString();
					if(tbody.find("."+date).length<=0 && date!=""){
						//debugger;
						//出团日期
						var input1="<input  id=\"groupOpenDate"+index+"\" name=\"groupOpenDate\" type=\"text\" value=\""+date+"\" readonly='' />";
						//截团日期
						var $input2="<input id=\"groupCloseDate"+index+"\" name=\"groupCloseDate\" type=\"text\" onClick=\"WdatePicker({maxDate:getMinDate(this)})\" />";
						//团号
						var input3="<input class='gc' id=\"groupCode"+index+"\" name=\"groupCode\" type=\"text\" maxlength=\"500\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\" onKeyUp=\"replaceStr(this)\"/>";
						//签证国家
						var input4="<input id=\"visaCountry"+index+"\" name=\"visaCountry\" type=\"text\" >";
						//材料截止日期
						var input5="<input id=\"visaDate"+index+"\" name=\"visaDate\" type=\"text\" onClick=\"WdatePicker({maxDate:takeVisaDate(this)})\"/>";
						//同业价人价
						var input6="<input id=\"settlementAdultPrice"+index+"\" name=\"settlementAdultPrice\" type=\"text\" value=\""+$("#settlementAdultPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
						//同业价儿童
						var input7="<input id=\"settlementcChildPrice"+index+"\" name=\"settlementcChildPrice\" type=\"text\" value=\""+$("#settlementcChildPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
						//同业价特殊
						var input14="<input id=\"settlementSpecialPrice"+index+"\" name=\"settlementSpecialPrice\" type=\"text\" value=\""+$("#settlementSpecialPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
//						//trekiz成人价
//						var input8="<input id=\"trekizPrice"+index+"\" name=\"trekizPrice\" type=\"text\" value=\""+$("#trekizPriceDefine").val()+"\" class=\"rmb\" maxlength=\"8\"/>";
//						//trekiz儿童价
//						var input9="<input id=\"trekizChildPrice"+index+"\" name=\"trekizChildPrice\" type=\"text\" value=\""+$("#trekizChildPriceDefine").val()+"\" class=\"rmb\" maxlength=\"8\"/>";
						//建议零售价成人
						var input8="<input id=\"suggestAdultPrice"+index+"\" name=\"suggestAdultPrice\" type=\"text\" value=\""+$("#suggestAdultPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
						//建议零售价儿童
						var input9="<input id=\"suggestChildPrice"+index+"\" name=\"suggestChildPrice\" type=\"text\" value=\""+$("#suggestChildPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
						//建议零售价特殊
						var input15="<input id=\"suggestSpecialPrice"+index+"\" name=\"suggestSpecialPrice\" type=\"text\" value=\""+$("#suggestSpecialPriceDefine").val()+"\" class=\"rmb\" maxlength=\"14\"/>";
						//需交订金
						var input10="<input id=\"payDeposit"+index+"\" name=\"payDeposit\" type=\"text\" value=\""+$("#payDepositDefine").val()+"\" class=\"rmb\" maxlength=\"8\"/>";
						//单房差
						var input11="<input id=\"singleDiff"+index+"\" name=\"singleDiff\" type=\"text\" value=\""+$("#singleDiffDefine").val()+"\" class=\"rmb\" maxlength=\"8\"/>";
						//预收人数
						//值联动onblur=\"comparePosition(this)\" 
						var input12="<input id=\"planPosition"+index+"\" name=\"planPosition\" type=\"text\" maxlength=\"3\"/>";
						//空位数量
						var input13="<input id=\"freePosition"+index+"\" name=\"freePosition\" type=\"text\" maxlength=\"3\"/>";
						//操作
						var opertion="<a class=\"a1\" href=\"javascript:void(0)\" onclick=\"delGroupDate(this)\">删除</a><input type=\"hidden\" value=\"\" name=\"openDateFiles\" />";
						var trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+
								"</td><td>"+input6+"</td><td>"+input7+"</td><td>"+input14+"</td><td>"+input8+"</td><td>"+input9+"</td><td>"+input15+"</td><td>"+input10+"</td><td>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion+"</td></tr>";
						$("#contentTable tbody:eq(0)").append(trhtml);
						index++;
					}
				});

				$targetVal = $(opts.target).val();
				$disableVals = $targetVal;
				rewriteDate($targetVal,dateStart,dateEnd,groupTable);
			}
		}


		function getClicka(){
			//debugger;
			var columnsNum = 0;
			$("#contentTable thead tr:eq(0)").find("th").each(function(){
				if($(this).text().trim() != '推荐'){
					if($(this).attr('colspan')){
						columnsNum = columnsNum + parseInt($(this).attr('colspan'));
					}else{
						columnsNum = columnsNum + 1;
					}
				}else{
					columnsNum = columnsNum + 1;
				}
			});
			columnsNum = columnsNum -3;
			var closeBeforeDays = opts.closeBeforeDays;
			var visaBeforeDays = opts.visaBeforeDays;
			$targetVal = $(opts.target).val();
			var cruiseBoolean = $("#spaceType").length>0?true:false;
			if($targetVal!=""){
				$(closeBeforeDays).attr("disabled",false);
				$(visaBeforeDays).attr("disabled",false);
				$(opts.visaCountryCopy).attr("disabled",false);
				$(opts.visaCopyBtn).attr("disabled",false);
				$(opts.planPositionCopy).attr("disabled",false);
				$(opts.planPositionBtn).attr("disabled",false);
				$(opts.freePositionCopy).attr("disabled",false);
				$(opts.freePositionBtn).attr("disabled",false);

				// 对应需求号  0258
				// 发票税  复制  的开启与显示
				//$(opts.invoiceTaxCopy).attr("disabled",false);
				//$(opts.invoiceTaxBtn).attr("disabled",false);
				$("#invoiceTaxCopy").attr("disabled",false);
				$("#invoiceTaxBtn").attr("disabled",false);


				var empty = $("#contentTable tbody:eq(0)").find("tr[id='emptygroup']");
				if(empty.length!=0)
					$(empty[0]).remove();
				var tbody = $("#contentTable tbody:eq(0)");
				var dates = $targetVal.split(",");
				$(dates).each(function(i,value){
					var date = value.toString();
					var suggestBoolean = $("#suggestAdultPriceDefine").length>0?true:false;
					if(date!=""&&tbody.find("."+date).length<=0){
						var kind = $("#activityKind").val();  // 产品类型
						var groupPriceFlag = $("#groupPriceFlag").val();  //是否添加“价格方案”功能

						//全选
						var input_select = '<input class="none-height-input" name="ids" type="checkbox">'
						//出团日期
						var input1="<input width=\"9%\" id=\"groupOpenDate"+index+"\" name=\"groupOpenDate\" type=\"text\" value=\""+date+"\" readonly='' />";
						//截团日期
						var $input2="<input id=\"groupCloseDate"+index+"\" name=\"groupCloseDate\" type=\"text\" onClick=\"WdatePicker({maxDate:getMinDate(this)})\" />";
						//yingFuDate
						var inputYingfu="<input id=\"yingFuDate"+index+"\" name=\"yingFuDate\" type=\"text\"  />";

						//团号
						var groupCode = getMaxCount(date);
						//青岛凯撒单团，散拼境外游生成团号，境内游手动输入团号
						/*if(($("#companyUUID").val()=='7a8177e377a811e5bc1e000c29cf2586'
						 //大洋旅游单团，散拼手动输入团号
						 || $("#companyUUID").val()=='7a81a03577a811e5bc1e000c29cf2586'
						 //拉美图单团，散拼，大客户，自由行，游轮手动输入团号
						 || $("#companyUUID").val()=='7a81a26b77a811e5bc1e000c29cf2586'
						 //大唐国旅(友创国际)团期类产品手动输入团号
						 || $("#companyUUID").val()=='7a45838277a811e5bc1e000c29cf2586'
						 //诚品旅游团期类产品手动输入团号
						 || $("#companyUUID").val()=='ed88f3507ba0422b859e6d7e62161b00'
						 //日信观光团期类产品手动输入团号
						 || $("#companyUUID").val()=='58a27feeab3944378b266aff05b627d2'
						 //懿洋假期团期类产品手动输入团号
						 || $("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'
						 //非常国际	团期类产品手动输入团号
						 || $("#companyUUID").val()=='1d4462b514a84ee2893c551a355a82d2'
						 //优加国际	团期类产品手动输入团号
						 || $("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586'
						 //起航假期	团期类产品手动输入团号
						 || $("#companyUUID").val()=='5c05dfc65cd24c239cd1528e03965021')
						 && groupCode==''){
						 var input3="<input width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"50\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\" onKeyUp=validateLong(this) />";
						 }else{
						 var input3="<input width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"500\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\"  readonly=\"\" onKeyUp=\"replaceStr(this)\"/>";
						 }*/

						/**
						 *
						 * 对应需求号 460
						 *
						 * 说明：
						 * 除了青岛凯撒单团，散拼境外游生成团号，境内游手动输入团号
						 * 其他的 的供应商的团号规则都按照  配置来决定手输入  还是  自动
						 *
						 * groupCodeRuleDT  0：
						 *
						 */
						//青岛凯撒单团，散拼境外游生成团号，境内游手动输入团号
						if(($("#companyUUID").val()=='7a8177e377a811e5bc1e000c29cf2586')&& groupCode==''){ //青岛凯撒 特殊处理（境内）
							var input3="<input class='gc' width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"50\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\" onKeyUp=validateLong(this) />";
						}else{
							if($("#companyUUID").val()=='7a8177e377a811e5bc1e000c29cf2586'){//青岛凯撒 特殊处理（境外）
								var input3="<input class='gc' width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"500\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\"  readonly=\"\" onKeyUp=\"replaceStr(this)\"/>";
							}else{ //其他按照配置
								if(groupCodeRuleDT == 0){
									var input3="<input class='gc' width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"50\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\" onKeyUp=validateLong(this) />";
								}else{
									var input3="<input class='gc' width=\"7%\" id=\"groupCode"+index+"\" name=\"groupCode\" value=\""+ groupCode +"\" type=\"text\" maxlength=\"500\" title=\""+ groupCode +"\" onblur=\"removeCodeCss(this)\" onafterpaste=\"replaceStr(this)\"  readonly=\"\" onKeyUp=\"replaceStr(this)\"/>";
								}
							}
						}



						//签证国家
						var input4="<input width=\"9%\" id=\"visaCountry"+index+"\" name=\"visaCountry\" type=\"text\" >";
						//材料截止日期,minDate:getCurDate()
						var input5="<input width=\"9%\" id=\"visaDate"+index+"\" name=\"visaDate\" type=\"text\" onClick=\"WdatePicker({maxDate:takeVisaDate(this)})\"/>";
						//同业价人价
						var input6="<span>" + $("#settlementAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"settlementAdultPrice"+index+"\" name=\"settlementAdultPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementAdultPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementAdultPriceDefine")+"\"  maxlength=\"14\"/>";
						//同业价儿童
						var input7="<span>" + $("#settlementcChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"settlementcChildPrice"+index+"\" name=\"settlementcChildPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementcChildPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"14\"/>";
						if(!cruiseBoolean) {
							//同业价特殊
							var input14="<span>" + $("#settlementSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"settlementSpecialPrice"+index+"\" name=\"settlementSpecialPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementSpecialPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"14\"/>";
						}
//						//trekiz成人价
//						var input8="<input id=\"trekizPrice"+index+"\" name=\"trekizPrice\" type=\"text\" value=\""+$("#trekizPriceDefine").val()+"\" class=\""+$("#trekizPriceDefine").attr("class")+"\" var=\""+getCurrencyId("#trekizPriceDefine")+"\" maxlength=\"6\"/>";
//						//trekiz儿童价
//						var input9="<input id=\"trekizChildPrice"+index+"\" name=\"trekizChildPrice\" type=\"text\" value=\""+$("#trekizChildPriceDefine").val()+"\" class=\""+$("#trekizChildPriceDefine").attr("class")+"\" var=\""+getCurrencyId("#trekizChildPriceDefine")+"\" maxlength=\"6\"/>";
						if(suggestBoolean) {
							//建议零售价成人
							var input8="<span>" + $("#suggestAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"suggestAdultPrice"+index+"\" name=\"suggestAdultPrice\" type=\"text\" class=\"ipt-currency\" value=\""+$("#suggestAdultPriceDefine").val()+"\" var=\""+getCurrencyId("#suggestAdultPriceDefine")+"\" maxlength=\"14\"/>";
							//建议零售价儿童
							var input9="<span>" + $("#suggestChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"suggestChildPrice"+index+"\" name=\"suggestChildPrice\" type=\"text\" class=\"ipt-currency\" value=\""+$("#suggestChildPriceDefine").val()+"\" var=\""+getCurrencyId("#suggestChildPriceDefine")+"\" maxlength=\"14\"/>";
							if(!cruiseBoolean) {
								//建议零售价特殊
								var input15="<span>" + $("#suggestSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"suggestSpecialPrice"+index+"\" name=\"suggestSpecialPrice\" type=\"text\" class=\"ipt-currency\" value=\""+$("#suggestSpecialPriceDefine").val()+"\" var=\""+getCurrencyId("#suggestSpecialPriceDefine")+"\" maxlength=\"14\"/>";
							}
						}
						//t1t2增加供应价服务费计算，在QUAUQ价基础上增加1%的交易服务费
						//var rate = '${chargeRate}';
						//供应价成人
						/*var settlementAdultPrice = "" , supplyAdultPriceDefine = "";
						 if("" != $("#settlementAdultPriceDefine").val()) {
						 settlementAdultPrice = parseFloat($("#settlementAdultPriceDefine").val());
						 supplyAdultPriceDefine = settlementAdultPrice * 0.01 + settlementAdultPrice;
						 supplyAdultPriceDefine = xround(supplyAdultPriceDefine,2);
						 }*/
						//供应价儿童
						/*var settlementcChildPrice = "" , supplyChildPriceDefine = "";
						 if("" != $("#settlementcChildPriceDefine").val()) {
						 settlementcChildPrice = parseFloat($("#settlementcChildPriceDefine").val());
						 supplyChildPriceDefine = settlementcChildPrice * 0.01 + settlementcChildPrice;
						 supplyChildPriceDefine = xround(supplyChildPriceDefine,2);
						 }*/
						//供应价特殊人群
						/*var settlementSpecialPrice = "", supplySpecialPriceDefine = "";
						 if("" != $("#settlementSpecialPriceDefine").val()) {
						 settlementSpecialPrice = parseFloat($("#settlementSpecialPriceDefine").val());
						 supplySpecialPriceDefine = settlementSpecialPrice * 0.01 + settlementSpecialPrice;
						 supplySpecialPriceDefine = xround(supplySpecialPriceDefine,2);
						 }*/
						//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
						//同业quauq价成人，20160713修改发布产品时，quauq价不取值于同行价，quauq价不能进行修改
						//var input23="<span>" + $("#settlementAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"quauqAdultPrice"+index+"\" name=\"quauqAdultPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementAdultPriceDefine")+"\"  maxlength=\"14\" readonly=\"true\"/>";
						//同业quauq价儿童
						//var input24="<span>" + $("#settlementcChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"quauqChildPrice"+index+"\" name=\"quauqChildPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//同业quauq价特殊
						//var input25="<span>" + $("#settlementSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"quauqSpecialPrice"+index+"\" name=\"quauqSpecialPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//供应价成人
						//var input26="<span>" + $("#settlementAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"supplyAdultPrice"+index+"\" name=\"supplyAdultPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementAdultPriceDefine")+"\"  maxlength=\"14\" readonly=\"true\"/>";
						//供应价儿童
						//var input27="<span>" + $("#settlementcChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"supplyChildPrice"+index+"\" name=\"supplyChildPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//供应价特殊
						//var input28="<span>" + $("#settlementSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"supplySpecialPrice"+index+"\" name=\"supplySpecialPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";

						//var input23="<span>" + $("#settlementAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"quauqAdultPrice"+index+"\" name=\"quauqAdultPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementAdultPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementAdultPriceDefine")+"\"  maxlength=\"14\" readonly=\"true\"/>";
						//同业quauq价儿童
						//var input24="<span>" + $("#settlementcChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"quauqChildPrice"+index+"\" name=\"quauqChildPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementcChildPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//同业quauq价特殊
						//var input25="<span>" + $("#settlementSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"quauqSpecialPrice"+index+"\" name=\"quauqSpecialPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+$("#settlementSpecialPriceDefine").val()+"\" var=\""+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//供应价成人
						//var input26="<span>" + $("#settlementAdultPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"supplyAdultPrice"+index+"\" name=\"supplyAdultPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+supplyAdultPriceDefine+"\" var=\""+getCurrencyId("#settlementAdultPriceDefine")+"\"  maxlength=\"14\" readonly=\"true\"/>";
						//供应价儿童
						//var input27="<span>" + $("#settlementcChildPriceDefine").prev("span").clone().html() + "</span><input width=\"3%\" id=\"supplyChildPrice"+index+"\" name=\"supplyChildPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+supplyChildPriceDefine+"\" var=\""+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";
						//供应价特殊
						//var input28="<span>" + $("#settlementSpecialPriceDefine").prev("span").clone().html() + "</span><input width=\"4%\" id=\"supplySpecialPrice"+index+"\" name=\"supplySpecialPrice\" onkeyup=\"validNum(this)\" onafterpaste=\"validNum(this)\" type=\"text\" class=\"ipt-currency\" value=\""+supplySpecialPriceDefine+"\" var=\""+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"14\" readonly=\"true\"/>";


						//儿童最高人数
						var input21= "<input width=\"7%\" "+ "onafterpaste=\"this.value=this.value.replace(/\\D|^0.+/g,'')\" onkeyup=\"this.value=this.value.replace(/\\D|^0.+/g,'')\""
								+" id=\"maxChildrenCount"+index+"\" name=\"maxChildrenCount\" type=\"text\" value=\""+$("#maxChildrenCountDefine").val()+"\" maxlength=\"8\"/>";

						//特殊人群最高人数
						var input20= "<input width=\"7%\" "+ "onafterpaste=\"this.value=this.value.replace(/\\D|^0.+/g,'')\" onkeyup=\"this.value=this.value.replace(/\\D|^0.+/g,'')\""
								+" id=\"maxPeopleCount"+index+"\" name=\"maxPeopleCount\" type=\"text\" value=\""+$("#maxPeopleCountDefine").val()+"\" maxlength=\"8\"/>";

						var input_adultDiscountPrice="<span>"
								+ $("#settlementAdultPriceDefine").prev("span").clone().html()
								+ "</span><input width=\"4%\" id=\"adultDiscountPrice"+index
								+ "\" name=\"adultDiscountPrice\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""
								+getCurrencyId("#settlementAdultPriceDefine")+"\" maxlength=\"8\"/>";
						var input_childDiscountPrice="<span>"
								+ $("#settlementcChildPriceDefine").prev("span").clone().html()
								+ "</span><input width=\"4%\" id=\"childDiscountPrice"+index
								+ "\" name=\"childDiscountPrice\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""
								+getCurrencyId("#settlementcChildPriceDefine")+"\" maxlength=\"8\"/>";
						var input_specialDiscountPrice="<span>"
								+ $("#settlementSpecialPriceDefine").prev("span").clone().html()
								+ "</span><input width=\"4%\" id=\"specialDiscountPrice"+index
								+ "\" name=\"specialDiscountPrice\" type=\"text\" class=\"ipt-currency\" value=\"\" var=\""
								+getCurrencyId("#settlementSpecialPriceDefine")+"\" maxlength=\"8\"/>";

						// 对应需求号  223	
						var input_cruiseGroupControl = "<input type='hidden' name='cruiseGroupControlId' value='' />";

						//需交订金
						var input10="<span>" + $("#payDepositDefine").prev("span").clone().html() + "</span><input width=\"7%\" id=\"payDeposit"+index+"\" name=\"payDeposit\" type=\"text\" value=\""+$("#payDepositDefine").val()+"\" class=\"ipt-currency\" var=\""+getCurrencyId("#payDepositDefine")+"\" maxlength=\"8\"/>";
						//单房差
						var input11="<span>" + $("#singleDiffDefine").prev("span").clone().html() + "</span><input width=\"7%\" id=\"singleDiff"+index+"\" name=\"singleDiff\" type=\"text\" value=\""+$("#singleDiffDefine").val()+"\" class=\"ipt-currency\" var=\""+getCurrencyId("#singleDiffDefine")+"\" maxlength=\"8\"/>";
						//预收人数 onblur=\"comparePosition(this)\"
						var input12="<input width=\"4%\" id=\"planPosition"+index+"\" name=\"planPosition\" type=\"text\" maxlength=\"3\"/>";
						//空位数量
						var input13="<input width=\"4%\" id=\"freePosition"+index+"\" name=\"freePosition\" type=\"text\" maxlength=\"3\"/>";


						//发票税    懿洋假期  对应需求号   0258  
						var input22="<input width=\"2%\" id=\"invoiceTax"+index+"\" name=\"invoiceTax\" type=\"text\"  style=\"width: 19px;\" onafterpaste=\"checkValue(this)\" onKeyUp=\"checkValue(this)\" onfocus=\"checkValue(this)\" />%";


						//推荐
						// var input16="<input width=\"4%\" id=\"recommend"+index+"\" name=\"recommend\" type=\"checkbox\" value="+index+" />";
						//房型酒店
						var input17="<div name='hotelhouseDiv'><div style='display:inline-block;'><label>酒店：</label><input width=\"2%\" id=\"groupHotel"+index+"\" name=\"groupHotel\" value=\"\" type=\"text\" style=\"width:42px;padding-left:2px;padding-right:2px;margin-left:1px;\" /></div>" +
								"<div style='display:inline-block;'><label>房型：</label><input width=\"2%\" id=\"groupHouseType"+index+"\" name=\"groupHouseType\" value=\"\" type=\"text\" style=\"width:42px;padding-left:2px;padding-right:2px;margin-left:1px;\" /></div>" +
								"<em class='add-select' onclick='addHotelAndHouseType(this)' style='height: 19px;'></em></div>";
						var td17="";
						if (groupPriceFlag && groupPriceFlag == "true") {
							td17="<td name='hotelhouse'>"+input17+"</td>";
						}

						//操作   c463     添加备注按钮  groupNoteTipImg

						var opertion = "";
						opertion = opertion  + '<dl class="handle">'
						opertion = opertion  + '<dt>'
						opertion = opertion  + '<img title="操作" src="' +
								ctxStatic +
								'/images/handle_cz.png">'
						opertion = opertion  + '</dt>'
						opertion = opertion  + '<dd class="">'
						opertion = opertion  + '<p style="width: 72px">'
						opertion = opertion  + '<span></span>'
						opertion = opertion  + '<!--<a class="addSameDayGroup"-->'
						opertion = opertion  + '<!--href="javascript:void(0)">新增同日团期-->'
						opertion = opertion  + '<!--</a>-->'
						opertion = opertion  + '<!--<a class="a1" href="javascript:void(0)"-->'
						opertion = opertion  + '<!--onclick="delGroupDate(this)">删除-->'
						opertion = opertion  + '<!--</a>-->'
						opertion = opertion  + '<!--<a class="groupNote" href="javascript:void(0)">-->'
						opertion = opertion  + '<!--备注-->'
						opertion = opertion  + '<!--<em class="groupNoteTipImg"></em>-->'
						opertion = opertion  + '<!--</a>-->'
						opertion = opertion  + '<!--S--C109--需求设置优惠-->'
						opertion = opertion	 +'<a class=\"a1\" href=\"javascript:void(0)\" onclick=\"delGroupDate($(this).parent().parent().parent())\">删除 </a>' ;
						opertion = opertion	 +'<input type=\"hidden\" value=\"\" name=\"openDateFiles\" />';
						opertion=  opertion  + "<a class=\"groupNote\" href=\"javascript:void(0)\">备注<em class=\"\"></em></a>";

						//操作   223  游轮团控    只有游轮产品
						if(cruiseBoolean) {
							if(isneedCruiseGroupControl == '1'){
								opertion = opertion  + '<a class="a1" href="javascript:void(0)"  onclick="jbox_create_group_control_pop(this);">生成团控表</a>';
								opertion = opertion  + '<a class="a1" href="javascript:void(0)" onclick="jbox_view_group_control_pop(this);">查看关联团控表</a>';
							}
						}


						if(suggestBoolean && !cruiseBoolean && ($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5'))// 散拼的时间拼接下面的条件
						{
							opertion = opertion  + '<a class="a1" href="javascript:void(0)"onclick="jbox__view_discount_setting_pop_fab(this);">查看优惠额度</a>'
							opertion = opertion  + '<a class="a1" href="javascript:void(0)"onclick="jbox__modify_discount_setting_pop_fab(this);">修改优惠额度</a>'
						}
						opertion = opertion  + '<!--E--C109--需求设置优惠-->'
						if (groupPriceFlag == "true") {
							opertion = opertion  + '<a name=\"expandPricing\">展开价格方案</a>'
						}
						opertion = opertion  + '</p>'
						opertion = opertion  + '</dd>'
						opertion = opertion  + '</dl>'


						if(cruiseBoolean) {
							//游轮产品舱型

							//c463  复制本行  和  备注   亦可调用   activityForm.jsp  中的  addNewCabintype
							var spaceType = $("#spaceType").clone().show().prop('outerHTML');
							opertion = "<a class=\"a1\" href=\"javascript:void(0)\" onClick=\"addNewCabintype(this);\">新增舱型</a><a href=\"javascript:;\"> | </a>" + opertion;
						}
						var trhtml;
						var tuijian = $("#tuijian").val();  //是否添加“推荐”功能
						//散拼
						//debugger;
						if(suggestBoolean && !cruiseBoolean) {
							//debugger;
							if(68 == $("#companyId").val()){
								//debugger;
								if(tuijian == 'true'){
									trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>" +inputYingfu+"</td><td>"    +input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
											+ td17
											+"<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"
											+input7+"</td><td class='tr tdCurrency'>"
											+input14+"</td><td class='tr tdCurrency'>"
											+input8+"</td><td class='tr tdCurrency'>"
											+input9+"</td><td class='tr tdCurrency'>"
											+input15+"</td><td class='tr tdCurrency'>"

												//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
												/*+input23+"</td><td class='tr tdCurrency'>"
												 +input24+"</td><td class='tr tdCurrency'>"
												 +input25+"</td><td class='tr tdCurrency'>"
												 +input26+"</td><td class='tr tdCurrency'>"
												 +input27+"</td><td class='tr tdCurrency'>"
												 +input28+"</td><td class='tr tdCurrency'>"*/

											+input21+"</td><td class='tr tdCurrency'>"
											+input20+"</td><td class='tr tdCurrency'>"
											+input10+"</td><td class='tr tdCurrency'>"
											+input11+"</td><td>"
											+input12+"</td><td>"+input13+"</td><td>"+opertion
											+"</td>"
									"</tr>";
								}else{
									trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
											+ td17
											+"<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"
											+input7+"</td><td class='tr tdCurrency'>"
											+input14+"</td><td class='tr tdCurrency'>"
											+input8+"</td><td class='tr tdCurrency'>"
											+input9+"</td><td class='tr tdCurrency'>"
											+input15+"</td><td class='tr tdCurrency'>"

												//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
												/*+input23+"</td><td class='tr tdCurrency'>"
												 +input24+"</td><td class='tr tdCurrency'>"
												 +input25+"</td><td class='tr tdCurrency'>"
												 +input26+"</td><td class='tr tdCurrency'>"
												 +input27+"</td><td class='tr tdCurrency'>"
												 +input28+"</td><td class='tr tdCurrency'>"*/

											+input21+"</td><td class='tr tdCurrency'>"
											+input20+"</td><td class='tr tdCurrency'>"
											+input10+"</td><td class='tr tdCurrency'>"
											+input11+"</td><td>"
											+input12+"</td><td>"+input13+"</td><td>"+opertion
											+"</td>" +
											"</tr>";
								}

								//c463  添加 备注信息   处理备注 输入框   散拼
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								//对应需求号  0426  t1t2 打通    添加quauq 价、供应价    越柬行踪
								//trhtml += "<td colspan=\"15\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a> &nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";


								var $tr = $(trhtml);
								var lastInput3 = $("#contentTable tbody:eq(0)").find('tr input[name="groupCode"]').last();
								var xx  = groupCode.substring(0,4);
								if(lastInput3.length>0 ){
									var yy =	 $("input[value^='"+xx+"']").last().val();
									if(yy == undefined)
									{

										$tr.find('input[name="groupCode"]').val(groupCode);
									}
									else
									{
										var ct  =Number(yy.split('-')[1])+1;
										var st = ("000"+ct).substr(-4);
										$tr.find('input[name="groupCode"]').val(groupCode.split('-')[0]+'-'+st+'-'+groupCode.split('-')[2]);
									}
								}

								$("#contentTable tbody:eq(0)").append($tr);
							}
							else if ($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586'){ //越柬行踪
								if(groupCode==''){
									top.$.jBox.info("此区域及出团日期已发999条产品", "警告");
									return;
								}
								if(tuijian == 'true'){
									/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-s*/
									/*trhtml="<tr class='"+date+"'><td>"+input16+"</td><td>"+input1+"</td><td>"+$input2+"</td><td style=\"display:none;\">" +inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
									 + td17
									 +"<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input15+"</td><td class='tr tdCurrency'>"+input20+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion
									 +"</td>" +
									 "</tr>";*/
									trhtml="<tr class='"+date+"'><td>"+$input2+"</td><td>" +inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
											+ td17
											+"<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input15+"</td><td class='tr tdCurrency'>"


												//对应需求号  0426  t1t2 打通    添加quauq 价、供应价    越柬行踪
												/*+input23+"</td><td class='tr tdCurrency'>"
												 +input24+"</td><td class='tr tdCurrency'>"
												 +input25+"</td><td class='tr tdCurrency'>"
												 +input26+"</td><td class='tr tdCurrency'>"
												 +input27+"</td><td class='tr tdCurrency'>"
												 +input28+"</td><td class='tr tdCurrency'>"*/

											+input21+"</td><td class='tr tdCurrency'>"
											+input20+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion
											+"</td>" +
											"</tr>";
									/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-e*/
									//c463  添加 备注信息   处理备注 输入框
									trhtml += " <tr class=\"noteTr\" style=\"display: none\" > ";
									//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
									//trhtml += " <td colspan=\"14\"><div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
									trhtml += " <td colspan=\""+columnsNum+"\"><div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
									trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
									trhtml += " </tr>";

									$("#contentTable tbody:eq(0)").append(trhtml);
								}else{
									//处理bug#13135-产品决定不管是否是越柬和名扬批发商,应付账期和团号都展示-tgy-s
									trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
											+ td17
											+"<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input15+"</td><td class='tr tdCurrency'>"


												//对应需求号  0426  t1t2 打通    添加quauq 价、供应价   越柬行踪
												/*+input23+"</td><td class='tr tdCurrency'>"
												 +input24+"</td><td class='tr tdCurrency'>"
												 +input25+"</td><td class='tr tdCurrency'>"
												 +input26+"</td><td class='tr tdCurrency'>"
												 +input27+"</td><td class='tr tdCurrency'>"
												 +input28+"</td><td class='tr tdCurrency'>"*/
											+input21+"</td><td class='tr tdCurrency'>"
											+input20+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion
											+"</td>" +
											"</tr>";
									//处理bug#13135-产品决定不管是否是越柬和名扬批发商,应付账期和团号都展示-tgy-e
									//c463  添加 备注信息   处理备注 输入框
									trhtml += " <tr class=\"noteTr\" style=\"display: none\" > ";
									//trhtml += " <td colspan=\"15\"><div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
									//对应需求号  0426  t1t2 打通    添加quauq 价、供应价    越柬行踪
									trhtml += " <td colspan=\""+columnsNum+"\"><div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
									trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
									trhtml += " </tr>";

									$("#contentTable tbody:eq(0)").append(trhtml);
								}
							}
							else
							{
								if(tuijian == 'true'){
									trhtml="<tr class='"+date+"'>"
									if($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5')
									{
										trhtml += "<td>"+input_select+"</td>"
									}
									trhtml +="<td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
									trhtml += td17
									trhtml +="<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input15+"</td><td class='tr tdCurrency'>"


									//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
									/*+input23+"</td><td class='tr tdCurrency'>"
									 +input24+"</td><td class='tr tdCurrency'>"
									 +input25+"</td><td class='tr tdCurrency'>"
									 +input26+"</td><td class='tr tdCurrency'>"
									 +input27+"</td><td class='tr tdCurrency'>"
									 +input28+"</td><td class='tr tdCurrency'>"*/

									trhtml +=input21+"</td><td class='tr tdCurrency'>"
									trhtml +=input20+"</td><td class='tr tdCurrency'>"
									trhtml +=input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td>"

									//  团期  发票税  0258  懿洋假期
									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){
										trhtml +="<td>"+input22+"</td>"
									}

									trhtml +="<td>"+opertion+"</td>"
									if($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5')
									{
										trhtml += "<td   hidden='hidden'>"+input_adultDiscountPrice
										trhtml +="</td><td  hidden='hidden'>"+input_childDiscountPrice
										trhtml +="</td><td  hidden='hidden'>"+input_specialDiscountPrice
										trhtml +="</td>"
									}
									trhtml +="</tr>";
								}else{
									trhtml="<tr class='"+date+"'>"
									if($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5')
									{
										trhtml +="<td>"+input_select+"</td>"
									}
									trhtml +="<td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
									trhtml += td17
									trhtml +="<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input15+"</td><td class='tr tdCurrency'>"


									//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
									/*trhtml +=input23+"</td><td class='tr tdCurrency'>"
									 trhtml +=input24+"</td><td class='tr tdCurrency'>"
									 trhtml +=input25+"</td><td class='tr tdCurrency'>"
									 trhtml +=input26+"</td><td class='tr tdCurrency'>"
									 trhtml +=input27+"</td><td class='tr tdCurrency'>"
									 trhtml +=input28+"</td><td class='tr tdCurrency'>"*/

									trhtml +=input21+"</td><td class='tr tdCurrency'>"
									trhtml +=input20+"</td><td class='tr tdCurrency'>"
									trhtml +=input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td>"

									//  团期  发票税  0258  懿洋假期
									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){ //散拼
										trhtml +="<td>"+input22+"</td>"
									}

									trhtml +="<td>"+opertion
									trhtml +="</td>"
									if($("#companyUUID").val()=='7a81c5d777a811e5bc1e000c29cf2586' || $("#companyUUID").val()=='75895555346a4db9a96ba9237eae96a5')
									{
										trhtml +="<td  hidden='hidden' >"+input_adultDiscountPrice
										trhtml +="</td><td  hidden='hidden'>"+input_childDiscountPrice
										trhtml +="</td><td  hidden='hidden'>"+input_specialDiscountPrice
										trhtml +="</td>"
									}
									trhtml += "</tr>";
								}

								//TODO 也要根据istuijian减少或增加一列
								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								if(groupPriceFlag && groupPriceFlag == "true"){  //299V2添加一列，需增加	
									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//   0258需求   懿洋假期
										//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
										trhtml += " <td colspan=\""+columnsNum+"\">";
										//trhtml += " <td colspan=\"23\">";
									}else{
										//对应需求号  0426  t1t2 打通    添加quauq 价、供应价
										trhtml += " <td colspan=\""+columnsNum+"\">";
										//trhtml += " <td colspan=\"22\">";
									}
								} else {
									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//   0258需求   懿洋假期
										//对应需求号  0426  t1t2 打通    添加quauq 价、供应价

										trhtml += " <td colspan=\""+columnsNum+"\">";
									}else{
										//对应需求号  0426  t1t2 打通    添加quauq 价、供应价

										trhtml += " <td colspan=\""+columnsNum+"\">";
									}
								}
								trhtml += "<div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";

								$("#contentTable tbody:eq(0)").append(trhtml);

							}


							/*
							 * t1t2  打通,添加团期后   对quauq 价的处理
							 * 具体处理规则见方法注释
							 */
							//afterClicka(adultQuauqPriceStrategy,childrenQuauqPriceStrategy,spicalQuauqPriceStratety);


							//游轮
						} else if (cruiseBoolean) {
							//debugger;
							if(68 == $("#companyId").val())
							{
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td><td>" + spaceType + "</td>"
										+ "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion
								trhtml +="</td>"

								// 对应需求号  223
								if(isneedCruiseGroupControl == '1'){
									trhtml +="<td  hidden='hidden'>"+input_cruiseGroupControl
									trhtml +="</td>"
								}
								trhtml += "</tr>";



								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";


								var $tr = $(trhtml);
								var lastInput3 = $("#contentTable tbody:eq(0)").find('tr input[name="groupCode"]').last();
								var xx  = groupCode.substring(0,4);
								if(lastInput3.length>0 ){
									var yy =	 $("input[value^='"+xx+"']").last().val();
									if(yy == undefined)
									{

										$tr.find('input[name="groupCode"]').val(groupCode);
									}
									else
									{
										var ct  =Number(yy.split('-')[1])+1;
										var st = ("000"+ct).substr(-4);
										$tr.find('input[name="groupCode"]').val(groupCode.split('-')[0]+'-'+st+'-'+groupCode.split('-')[2]);
									}
								}
								$("#contentTable tbody:eq(0)").append($tr);
							}
							else if ($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586')
							{
								if(groupCode==''){
									top.$.jBox.info("此区域及出团日期已发999条产品", "警告");
									return;
								}
								/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-s*/
								/*trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td style=\"display:none;\">"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td><td>" + spaceType + "</td>"
								 + "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td>"

								 //  团期  发票税  0258

								 // trhtml +="<td>"+input22+"</td>"

								 trhtml +="<td>"+opertion
								 trhtml +="</td>";*/
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td><td>" + spaceType + "</td>"
										+ "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion
								trhtml +="</td>";
								/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-e*/
								// 对应需求号  223
								if(isneedCruiseGroupControl == '1'){
									trhtml +="<td  hidden='hidden'>"+input_cruiseGroupControl
									trhtml +="</td>"
								}
								trhtml += "</tr>";

								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";

								$("#contentTable tbody:eq(0)").append(trhtml);
							}
							else
							{
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td><td>" + spaceType + "</td>" + td17
										+ "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input8+"</td><td class='tr tdCurrency'>"+input9+"</td><td class='tr tdCurrency'>"

										+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td>"

								//  团期  发票税  0258  懿洋假期
								if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){ //游轮
									trhtml +="<td>"+input22+"</td>"
								}


								trhtml +="<td>"+opertion
								trhtml +="</td>"

								// 对应需求号  223
								if(isneedCruiseGroupControl == '1'){
									trhtml +="<td  hidden='hidden'>"+input_cruiseGroupControl
									trhtml +="</td>"
								}
								trhtml += "</tr>";


								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";

								if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){
									trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								}else{
									trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								}

								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";

								$("#contentTable tbody:eq(0)").append(trhtml);
							}

							//单团类
						}else{
							if(68 == $("#companyId").val())
							{
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
										+ td17
										+ "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"
										+input14+"</td><td class='tr tdCurrency'>"
										+input21+"</td><td class='tr tdCurrency'>"
										+input20+"</td><td class='tr tdCurrency'>"
										+input10+"</td><td class='tr tdCurrency'>"
										+input11+"</td><td>"+input12+"</td><td>"
										+input13+"</td><td>"+opertion+"</td></tr>";


								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								trhtml += "<td colspan=\""+columnsNum+"\"> <div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";


								var $tr = $(trhtml);
								var lastInput3 = $("#contentTable tbody:eq(0)").find('tr input[name="groupCode"]').last();

								var xx  = groupCode.substring(0,4);



								if(lastInput3.length>0 ){
									var yy =	 $("input[value^='"+xx+"']").last().val();
									if(yy == undefined)
									{

										$tr.find('input[name="groupCode"]').val(groupCode);
									}
									else
									{
										var ct  =Number(yy.split('-')[1])+1;
										var st = ("000"+ct).substr(-4);
										$tr.find('input[name="groupCode"]').val(groupCode.split('-')[0]+'-'+st+'-'+groupCode.split('-')[2]);
									}
								}

								$("#contentTable tbody:eq(0)").append($tr);

							}
							else if ($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586')
							{
								if(groupCode==''){
									top.$.jBox.info("此区域及出团日期已发布999条产品", "警告");
									return;
								}
								/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-s*/
								/*trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td style=\"display:none;\">"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
								 + td17
								 + "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input20+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion+"</td></tr>";*/
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
										+ td17
										+ "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"+input14+"</td><td class='tr tdCurrency'>"+input20+"</td><td class='tr tdCurrency'>"+input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"+input12+"</td><td>"+input13+"</td><td>"+opertion+"</td></tr>";
								/*因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-e*/
								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								trhtml += "<td colspan=\""+columnsNum+"\"><div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";

								$("#contentTable tbody:eq(0)").append(trhtml);
							}
							else
							{
								trhtml="<tr class='"+date+"'><td>"+input1+"</td><td>"+$input2+"</td><td>"+inputYingfu+"</td><td>"+input3+"</td><td>"+input4+"</td><td>"+input5+"</td>"
								trhtml += td17
								trhtml += "<td class='tr tdCurrency'>"+input6+"</td><td class='tr tdCurrency'>"+input7+"</td><td class='tr tdCurrency'>"
								trhtml += input14+"</td><td class='tr tdCurrency'>"
								trhtml +=input21+"</td><td class='tr tdCurrency'>"
								trhtml +=input20+"</td><td class='tr tdCurrency'>"
								trhtml +=input10+"</td><td class='tr tdCurrency'>"+input11+"</td><td>"
								trhtml +=input12+"</td><td>"+input13+"</td>"

								//  团期  发票税  0258  懿洋假期
								if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){ //单团系列
									trhtml +="<td>"+input22+"</td>"
								}


								trhtml +="<td>"+opertion+"</td></tr>";


								//c463  添加 备注信息   处理备注 输入框
								trhtml += " <tr class=\"noteTr\"  style=\"display: none\" > ";
								if (groupPriceFlag && groupPriceFlag == "true"){  //299V2添加一列，需增加

									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){  //对应需求号  0258  懿洋假期
										trhtml += "<td colspan=\""+columnsNum+"\">";
									}else{
										trhtml += "<td colspan=\""+columnsNum+"\">";
									}
								} else {

									if($("#companyUUID").val()=='f5c8969ee6b845bcbeb5c2b40bac3a23'){  //对应需求号  0258  懿洋假期
										trhtml += "<td colspan=\""+columnsNum+"\">";
									}else{
										trhtml += "<td colspan=\""+columnsNum+"\">";
									}
								}
								trhtml += "<div class=\"remarks-containers\">备注:<input type=\"text\" class=\"groupNotes\" name=\"groupRemark\" > <em class=\"clearNotes\"></em></input></div> </td>";
								trhtml += " <td class=\"tc\"><a href=\"javascript:void(0)\" class=\"saveNotes\">保存</a>&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"unSaveNotes\">取消</a></td>";
								trhtml += " </tr>";


								$("#contentTable tbody:eq(0)").append(trhtml);
								$(".gc").bind('paste', function(e) {
									var el = $(this);
									setTimeout(function() {
										replaceStr(el[0]);
									}, 100);
								});

							}

						}
						index++;
					}
				});
				rewriteDate($targetVal,dateStart,dateEnd,groupTable);


			}

		}


		/**
		 * 点击团期生成  团期记录后处理
		 * 1.如果同行价的值不为空  则  根据 匹配到的价格策略中的方案进行进行计算，得出quauq价（价格方案中的最低价）。
		 * 2.如果同行价没有进行填写，则quauq 价 不得进行修改，改为只读
		 * 3.修改quauq价，不得低高于，价格方案中的最低价，在点下一步（ 2-》3 步时进行校验）
		 * 4.价格策略的结构如下：
		 * adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
		 * childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
		 * spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折
		 *
		 *//*
		 function afterClicka(adultQuauqPriceStrategy,childrenQuauqPriceStrategy,spicalQuauqPriceStratety){

		 //1.获取新增行的  团期的  成了价    儿童价    特殊人群价
		 var settlementAdultPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementAdultPrice']").val();
		 var settlementcChildPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementcChildPrice']").val();
		 var settlementSpecialPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementSpecialPrice']").val();

		 //成人
		 if(""==settlementAdultPrice){
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqAdultPrice']").attr("readonly","readonly");
		 }else{
		 var adultQuauqPrice = getQuauqPrice(settlementAdultPrice,adultQuauqPriceStrategy);
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqAdultPrice']").val(adultQuauqPrice);
		 }

		 //儿童
		 if(""==settlementcChildPrice){
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqChildPrice']").attr("readonly","readonly");
		 }else{
		 var childrenQuauqPrice = getQuauqPrice(settlementcChildPrice,childrenQuauqPriceStrategy);
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqChildPrice']").val(childrenQuauqPrice);
		 }

		 //特殊人群
		 if(""==settlementSpecialPrice){
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
		 }else{
		 var spicalQuauqPrice = getQuauqPrice(settlementSpecialPrice,spicalQuauqPriceStratety);
		 $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqSpecialPrice']").val(spicalQuauqPrice);
		 }

		 }

		 *//**
		 * 根据价格方案获取策略价：最低quauq价
		 *
		 *//*
		 function getQuauqPrice(srcPrice,srcPriceStrategy){
		 debugger;
		 var srcPriceStrategyArray = srcPriceStrategy.split("#");
		 var quauqPriceArray = new Array();
		 for(var i = 0;i < srcPriceStrategyArray.length; i++) {
		 var quauqPrice = srcPrice;//
		 var priceStrategyArray =  srcPriceStrategyArray[i].split(",");
		 for(var j = 0;j < priceStrategyArray.length; j++) {
		 var  srcPriceStrategyItem = priceStrategyArray[j].split(":");
		 if("1" == srcPriceStrategyItem[0]){
		 quauqPrice = new Number(quauqPrice) + new Number(srcPriceStrategyItem[1]);
		 }else if("2" == srcPriceStrategyItem[0]){
		 quauqPrice = new Number(quauqPrice) - new Number(srcPriceStrategyItem[1]);
		 }else if("3" == srcPriceStrategyItem[0]){
		 quauqPrice = new Number(quauqPrice)*new Number(srcPriceStrategyItem[1])/100;
		 }
		 }
		 if(quauqPrice<0){
		 quauqPrice = 0;
		 }
		 quauqPriceArray.push(quauqPrice);
		 }
		 var minQuauqPrice = getMaxMinNum(quauqPriceArray,"min");
		 //var maxQuauqPrice = getMaxMinNum(quauqPriceArray,"max");
		 return  minQuauqPrice;

		 }

		 *//**
		 *
		 * @param arr:array operated
		 * @param type:expected max,min
		 * @returns get max/min value in specified array or cosole log error
		 *//*
		 function getMaxMinNum(arr,type){
		 if(type==''||type==null||type=='undefined'){
		 //console.log("Type is undefined.Please specified!");
		 return false;
		 }
		 if('max'==type){
		 return Math.max.apply(null,arr);
		 }
		 if('min'==type){
		 return Math.min.apply(null,arr);
		 }
		 }*/


		//获取币种类型的ID
		function getCurrencyId(inputId) {
			return $(inputId).parent().prev().find("p[class='p-checked']").attr("id");
		}

		//验证是否选取过
		function isClick(item){
			var aa = $(item);
			try{
				return aa.data("picker").clicked;
			}catch(ers){
				aa.data("picker",{clicked:false});
				return false;
			}
		}
		//fun1 $.fn.dynj=delGroup; 删除某个出团日期
		function delGroup(obj){

			//c463
			$(obj).parent().parent().next().remove();
			$(obj).parent().parent().next("td[name=groupPriceTd]").remove();
			var len= $("#contentTable").find("input[name='groupOpenDate']").length;
			var j = 1;
			for(var i = 0; i < len;i++) {
				if($($("#contentTable").find("input[name='groupOpenDate']")[i]).val()==$($("#contentTable").find("input[name='groupOpenDate']")[i+1]).val()){
					j++;
				}
			}
			var divobj = $(obj).parent().parent();
			var input = $(divobj).find("input[name='groupOpenDate']")[0];
			var date = $(input).val();
			if(len == 1) {
				$(".add2_nei_chosedate").find("[class*='"+date+"']").remove();
			}else {
				if(len != j) {
					$(".add2_nei_chosedate").find("[class*='"+date+"']").remove();
				}
			}
			var targetVal = $(opts.target).val();
			$(opts.target).val(targetVal.replace(date + ",",""));
			$disableVals = $disableVals.replace(date + ",","");
			$(divobj).remove();
			$targetVal = $(opts.target).val();
			rewriteDate($targetVal,dateStart,dateEnd,groupTable);
			var len = $("#contentTable tbody:eq(0)").find("tr").length;
			if(len==0){
				var kind = $("#activityKind").val();
				var tuijian = $("#tuijian").val();
				var closeBeforeDays = opts.closeBeforeDays;
				var visaBeforeDays = opts.visaBeforeDays;
				$(closeBeforeDays).attr("disabled",true);
				$(visaBeforeDays).attr("disabled",true);
				$(opts.visaCountryCopy).attr("disabled",true);
				$(opts.visaCopyBtn).attr("disabled",true);

				//修改bug15406时，将colspan改为动态取值------djw
				var columnsNum = 0;
				$("#contentTable thead tr:eq(0)").find("th").each(function(){
					if($(this).text().trim() != '推荐'){
						if($(this).attr('colspan')){
							columnsNum = columnsNum + parseInt($(this).attr('colspan'));
						}else{
							columnsNum = columnsNum + 1;
						}
					}else{
						columnsNum = columnsNum + 1;
					}
				});
				$("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"" + columnsNum + "\">暂无价格信息，请选择日期</td></tr>");
				/*if(kind == 2 & tuijian == 'true'){					
				 $("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"26\">暂无价格信息，请选择日期</td></tr>");
				 }else{
				 $("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"17\">暂无价格信息，请选择日期</td></tr>");
				 }*/
			}

			//$targetVal = $(opts.target).val();
			//writeDate($targetVal,dateStart,dateEnd,groupTable);
			rewriteDate($targetVal,dateStart,dateEnd,groupTable);
		}
		var i = 1;
		$.fn.extend({
			//从页面上删除团期
			delGroup1:function(obj){
				delGroup(obj);
			},
			//根据选择的日期画出团期
			writeDate:function(){
				getTrHtml();
			},clicka:function(td){
				getClicka();

				var date = $(td).children().attr("action");
				//在选择了日期之后，在选择日期按钮后拼接一个灰色区域
				var $selectedDate = $("input.selectedDate").first();
				if(date!="" && $(".add2_nei_chosedate").find("[class*='"+date+"']").length<=0){
					if($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586'){
						var groupCode = getMaxCount(date);
						if(groupCode!=''){
							$("input.selectedDate").last().after($selectedDate.clone().val(date).addClass(date).show());
						}
					}else{
						$("input.selectedDate").last().after($selectedDate.clone().val(date).addClass(date).show());
					}
				}else{
//					$(".add2_nei_chosedate").find(".inputTxt.selectedDate." + date).remove();
				}
			}
		});

		$.extend($.fn.datepickerRefactor,

				{
					initdisable:function(disableVals){
						$disableVals = disableVals;
						$modVals = disableVals;
						//$(opts.target).val(disableVals);
					},
					deldisable:function(date){
						if($disableVals.indexOf(date)>=0)
							$disableVals = $disableVals.replace(date+",","");
						/*$targetVal = $(opts.target).val();
						 if($targetVal.indexOf(date)>=0){
						 $targetVal = $targetVal.replace(date+",", "");
						 $(opts.target).val($targetVal);
						 }
						 $targetVal = $(opts.target).val();
						 rewriteDate($targetVal,dateStart,dateEnd,groupTable);*/
					},
					delAllGroupDate:function(){
						var kind = $("#activityKind").val();
						var tuijian = $("#tuijian").val();
//					$(opts.target).val($modVals);//10657的解决
						$(opts.target).val('');
						$targetVal = "";
						$disableVals = "";
						$("#contentTable tbody:eq(0)").children().remove();

						//修改bug15406时，将colspan改为动态取值------djw
						var columnsNum = 0;
						$("#contentTable thead tr:eq(0)").find("th").each(function(){
							if($(this).text().trim() != '推荐'){
								if($(this).attr('colspan')){
									columnsNum = columnsNum + parseInt($(this).attr('colspan'));
								}else{
									columnsNum = columnsNum + 1;
								}
							}else{
								columnsNum = columnsNum + 1;
							}
						});
						$("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"" + columnsNum + "\">暂无价格信息，请选择日期</td></tr>");
						/*if(kind == 2 & tuijian == 'true'){
						 $("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"" + columnsNum + "\">暂无价格信息，请选择日期</td></tr>");
						 }else{
						 $("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"" + columnsNum + "\">暂无价格信息，请选择日期</td></tr>");
						 }*/
						$(dateStart).val("");
						$(dateEnd).val("");
						//$targetVal = $(opts.target).val();//存在bug,所以注释掉
						rewriteDate($targetVal,dateStart,dateEnd,groupTable);
						$(".add2_nei_chosedate").children("input:visible").remove();
					}
				}
		);

		function delVal(){
			var willVal = $("#datepicker_input").val();
			var targetVal = $(opts.target).val();

			if(targetVal.indexOf(willVal) >= 0){
				$(opts.target).val(targetVal.replace(willVal + ",",""));

				$("#contentTable tbody:eq(0)").find("tr").each(function(index,obj){
					var input = $(obj).find("td").find("input[name='groupOpenDate']")[0];
					var tmp = $(input).val();
					if(tmp==willVal){


						//c463  日期第二次点击 时  删除 团期  前  先删除  团期备注
						$(this).next().remove();
						$(this).next("td[name=groupPriceTd]").remove();
						$(this).remove();

						//删除团期时删除“选择日期”后面日期
						$(".add2_nei_chosedate").find(".inputTxt.selectedDate." + tmp).remove();

						return false;
					}
				});
				var len = $("#contentTable tbody:eq(0)").find("tr").length;
				if(len==0)
					$("#contentTable tbody:eq(0)").append("<tr id=\"emptygroup\"><td colspan=\"24\">暂无价格信息，请选择日期</td></tr>");
			}
			$targetVal = $(opts.target).val();
		}

	};
	$.fn.datepickerRefactor.defaults = {
		target:"",
		focusColor:"#62afe7",
		blurColor:"#E6E6E6"
	};
	$.fn.datepickerRefactor.setDefaults = function(settings) {
		$.extend($.fn.datepickerRefactor.defaults, settings);
	};


	//fun2
	//$.fn.datepickerRefactor.delGroup=function(){
	//alert("ss");
	//};

});


/**
 *
 * @param {} obj
 */
var flag=10;
function validateLong(obj)
{
	replaceStr(obj);
	if($(obj).val().length<=49){
		flag = 10;
	}
	if($(obj).val().length>=50)
	{
		if($(obj).val().length==50 && flag==10){
			//$.jBox.tip("团号只能输入50个字符","true");
			flag++;
		}else{
			$.jBox.tip("团号超过50个字符，请修改","error");
		}
		return false;
	}
}






