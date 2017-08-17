//第三人调整后的报价结果数据结构
var newResult = {
	"detailList" : [ {
		"guestPriceList" : [ {
			"amount" : 300,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"travelerType" : "c89e0a6661b64d1e809d8873cf85bc80",
			"travelerTypeText" : "成人"
		}, {
			"amount" : 200,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"travelerType" : "0d72dcad18d849549dee4589f50bdc9e",
			"travelerTypeText" : "婴儿"
		}, {
			"amount" : 150,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "儿童"
		}, {
			"amount" : 120,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 1,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "第三人"
		}, {
			"amount" : 160,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 1,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "第四人"
		} ],
		"hotelMealText" : "BB",
		"hotelMealUuid" : "a8f70bba75174c3cb820848d4a157b0b",
		"hotelRoomName" : "Water Villa",
		"hotelRoomOccupancyRate" : "2A+2C/3A+1C",
		"hotelRoomUuid" : "108328bd4db141c99ff73559f1122e8b",
		"inDate" : "2015-09-01",
		"inDateForDate" : "2015-09-01"
	} ],
	"guestPriceList" : [ {
		"amount" : 500,
		"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"currencyText" : "$",
		"isThirdPerson" : 0,
		"travelerType" : "c89e0a6661b64d1e809d8873cf85bc80",
		"travelerTypeText" : "成人"
	}, {
		"amount" : 400,
		"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"currencyText" : "$",
		"isThirdPerson" : 0,
		"travelerType" : "0d72dcad18d849549dee4589f50bdc9e",
		"travelerTypeText" : "婴儿"
	}, {
		"amount" : 450,
		"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"currencyText" : "$",
		"isThirdPerson" : 0,
		"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
		"travelerTypeText" : "儿童"
	}, {
		"amount" : 220,
		"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"currencyText" : "$",
		"isThirdPerson" : 1,
		"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
		"travelerTypeText" : "第三人"
	}, {
		"amount" : 280,
		"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"currencyText" : "$",
		"isThirdPerson" : 1,
		"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
		"travelerTypeText" : "第四人"
	} ],
	"memo" : "1、以下价格为每间每晚的报价，已含服务费，但不含政府税，不含床税；从2014.11.01开始，政府税将由8%涨至12%，从2014.12.01开始取消$8床税；2、全年3晚起订；12.28-01.05期间4晚起订，不能在12.30或12.31退房；2014.12.25-2015.01.08期间不接受混住；其它时间若混住的话，每种房型2晚起订；",
	"mixlivePrice" : 50,
	"mixlivePriceCurrencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
	"mixlivePriceCurrencyText" : "$",
	"preferentialList4hotelPl" : [
			{
				"bookingCode" : "2BPV2OWPV001",
				"description" : "2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受住七付五，房费九折，含HB餐免二人交通优惠。",
				"preferentialName" : "住七付五房费九折优惠",
				"uuid" : "优惠1uuid"
			},
			{
				"bookingCode" : "2BPV2OWPV002",
				"description" : "2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受水上飞机9折优惠。",
				"preferentialName" : "交通优惠",
				"uuid" : "优惠2uuid"
			},
			{
				"bookingCode" : "2BPV2OWPV003",
				"description" : "赠送水果篮，1瓶香槟；入住期间参加自费项目可优惠$100，；赠送烛光晚餐，不含饮料。",
				"preferentialName" : "蜜月优惠",
				"uuid" : "优惠3uuid"
			},
			{
				"bookingCode" : "2BPV2OWPV004",
				"description" : "3N Deluxe Beach Villa+1N Water Villa 2015.10.6-2015.12.18期间打包价为$780/间",
				"preferentialName" : "打包优惠",
				"uuid" : "优惠4uuid"
			} ],
	"preferentialTotal" : [ {
		"guestPriceList" : [ {
			"amount" : 1200,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"preferAmount" : 200,
			"travelerType" : "c89e0a6661b64d1e809d8873cf85bc80",
			"travelerTypeText" : "成人"
		}, {
			"amount" : 500,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"preferAmount" : 50,
			"travelerType" : "0d72dcad18d849549dee4589f50bdc9e",
			"travelerTypeText" : "婴儿"
		}, {
			"amount" : 800,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 0,
			"preferAmount" : 100,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "儿童"
		}, {
			"amount" : 200,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 1,
			"preferAmount" : 20,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "第三人"
		}, {
			"amount" : 200,
			"currencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
			"currencyText" : "$",
			"isThirdPerson" : 1,
			"preferAmount" : 80,
			"travelerType" : "5750998f7ad044f89fd8a7e8966130e1",
			"travelerTypeText" : "第四人"
		} ],
		"mixlivePrice" : 50,
		"mixlivePriceCurrencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"mixlivePriceCurrencyText" : "$",
		"preferentialList" : [
				{
					"bookingCode" : "2BPV2OWPV001",
					"bookingEndDate" : "2015-12-31",
					"bookingEndDateString" : "2015-12-31",
					"bookingStartDate" : "2014-09-01",
					"bookingStartDateString" : "2014-09-01",
					"description" : "2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受住七付五，房费九折，含HB餐免二人交通优惠。",
					"inDate" : "2015-01-01",
					"inDateString" : "2015-01-01",
					"islandWayList" : [ {
						"label" : "水飞"
					} ],
					"matter" : {
						"memo" : "XXXXXXXXXXXXXXXXXXX",
						"preferentialTaxMap" : {
							"1" : [ {
								"chargeTypeText" : "%",
								"hotelMealText" : "",
								"islandWayText" : "",
								"istaxText" : "政府税，服务税，床税",
								"preferentialAmount" : 50,
								"preferentialTypeText" : "打折",
								"travelerTypeText" : "成人"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "",
								"islandWayText" : "",
								"istaxText" : "服务税，床税",
								"preferentialAmount" : 200,
								"preferentialTypeText" : "减金额",
								"travelerTypeText" : "儿童"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "",
								"islandWayText" : "",
								"istaxText" : "床税",
								"preferentialAmount" : 20,
								"preferentialTypeText" : "减最低",
								"travelerTypeText" : "婴儿"
							} ]
						},
						"preferentialTemplatesDetailText" : "住： 5  晚  免：1  晚",
						"preferentialTemplatesText" : "住宿优惠"
					},
					"outDate" : "2015-09-30",
					"outDateString" : "2015-09-30",
					"preferentialName" : "住七付五房费九折优惠",
					"preferentialRoomList" : [ {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Beachfront Villa",
						"nights" : 2,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					}, {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Water Bungalow",
						"nights" : 1,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					}, {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Water Bungalow",
						"nights" : 1,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					} ],
					"require" : {
						"applicableThirdPersonText" : "",
						"bookingNights" : 5,
						"bookingNumbers" : 10,
						"isSuperpositionText" : "",
						"memo" : "XXXXXXXXXXXXXXXXXXXXXXXX",
						"notApplicableDate" : "2015.01.01",
						"notApplicableRoomName" : "Beach Villa"
					},
					"uuid" : "优惠1uuid"
				},
				{
					"bookingCode" : "2BPV2OWPV002",
					"bookingEndDate" : "2015-12-31",
					"bookingEndDateString" : "2015-12-31",
					"bookingStartDate" : "2014-09-01",
					"bookingStartDateString" : "2014-09-01",
					"description" : "2015.10.6-2015.12.18期间预订2015.10.06-2015.12.25期间的Water Villa/Deluxe Beach Villa及其以上房型的房间可享受水上飞机9折优",
					"inDate" : "2015-01-01",
					"inDateString" : "2015-01-01",
					"matter" : {
						"memo" : "XXXXXXXXXXXXXXXXXXX",
						"preferentialTaxMap" : {
							"2" : [ {
								"chargeTypeText" : "%",
								"hotelMealText" : "",
								"islandWayText" : "水飞,内飞",
								"istaxText" : "政府税，服务税，床税",
								"preferentialAmount" : 50,
								"preferentialTypeText" : "打折",
								"travelerTypeText" : "成人"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "",
								"islandWayText" : "水飞",
								"istaxText" : "服务税，床税",
								"preferentialAmount" : 200,
								"preferentialTypeText" : "减金额",
								"travelerTypeText" : "儿童"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "",
								"islandWayText" : "内飞",
								"istaxText" : "床税",
								"preferentialAmount" : 20,
								"preferentialTypeText" : "减最低",
								"travelerTypeText" : "婴儿"
							} ],
							"3" : [ {
								"chargeTypeText" : "%",
								"hotelMealText" : "BB,HB,AI",
								"islandWayText" : "",
								"istaxText" : "政府税，服务税，床税",
								"preferentialAmount" : 50,
								"preferentialTypeText" : "打折",
								"travelerTypeText" : "成人"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "BB,HB,AI",
								"islandWayText" : "",
								"istaxText" : "服务税，床税",
								"preferentialAmount" : 200,
								"preferentialTypeText" : "减金额",
								"travelerTypeText" : "儿童"
							}, {
								"chargeTypeText" : "$",
								"hotelMealText" : "BB,HB,AI",
								"islandWayText" : "",
								"istaxText" : "床税",
								"preferentialAmount" : 20,
								"preferentialTypeText" : "减最低",
								"travelerTypeText" : "婴儿"
							} ],
							"4" : [ {
								"istaxText" : "政府税，服务税，床税"
							} ]
						},
						"preferentialTemplatesDetailText" : "住： 5  晚  免：1  晚",
						"preferentialTemplatesText" : "住宿优惠"
					},
					"outDate" : "2015-09-30",
					"outDateString" : "2015-09-30",
					"preferentialName" : "交通优惠",
					"preferentialRoomList" : [ {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Beachfront Villa",
						"nights" : 2,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					}, {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Water Bungalow",
						"nights" : 1,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					}, {
						"hotelMealList" : [ {
							"mealName" : "BB"
						}, {
							"mealName" : "HB"
						} ],
						"hotelRoomText" : "Water Bungalow",
						"nights" : 1,
						"relHotelName" : "关联酒店名称",
						"roomOccupancyRate" : "2A+2C"
					} ],
					"require" : {
						"applicableThirdPersonText" : "",
						"bookingNights" : 5,
						"bookingNumbers" : 10,
						"isSuperpositionText" : "",
						"memo" : "XXXXXXXXXXXXXXXXXXXXXXXX",
						"notApplicableDate" : "2015.01.01",
						"notApplicableRoomName" : "Beach Villa"
					},
					"uuid" : "优惠2uuid"
				} ],
		"totalPrice" : 12000,
		"totalPriceCurrencyId" : "c89e0a6661b64d1e809d8873cf85bc80",
		"totalPriceCurrencyText" : "$"
	} ],
	"preferentialUuids" : "选择的优惠UUID1,选择的优惠UUID2",
	"quotedPriceQuery" : {
		"arrivalIslandWay" : "c89e0a6661b64d1e809d8873cf85bc80",
		"arrivalIslandWayText" : "内飞",
		"country" : "c89e0a6661b64d1e809d8873cf85bc80",
		"countryText" : "马尔代夫",
		"departureIslandWay" : "c89e0a6661b64d1e809d8873cf85bc80",
		"departureIslandWayText" : "水飞",
		"hotelGroupText" : "万豪集团",
		"hotelText" : "万豪大酒店",
		"hotelUuid" : "c89e0a6661b64d1e809d8873cf85bc80",
		"islandText" : "太阳岛",
		"islandUuid" : "c89e0a6661b64d1e809d8873cf85bc80",
		"mixliveNum" : 2,
		"personNum" : [ "4", "0", "0" ],
		"position" : 1,
		"purchaseType" : 1,
		"purchaseTypeText" : "内采",
		"roomNum" : 1,
		"supplierInfoId" : 2,
		"supplierInfoText" : "天马"
	}
};
// 列表中需要第三人的title
var thirdPersonObj = [ {
	"guestType" : "c89e0a6661b64d1e809d8873cf85bc80",
	"guestTypeText" : "第三人"
}, {
	"guestType" : "32ds0a6661b64d1e809d8873cf85bc80",
	"guestTypeText" : "第四人"
} ]
