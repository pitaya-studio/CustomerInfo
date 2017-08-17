var updateFlag=0;
$(document).ready(function(){

    if(updateFlag){

        //基本信息
        //todo 需要后台处理数据
        baseInfo={
            "areaType": 1,
            "contactPhone": "联系电话",
            "country": "国家uuid",
            "countryText":"国家显示文本",
            "createByText": "创建人文本显示",
            "currencyId": 2,
            "currencyMark": "$",
            "currencyText": "币种符号显示文本",
            "galamealMemo": "节日餐备注",
            "hotelAddress": "酒店地址",
            "hotelGroup": "酒店集团",
            "hotelStar": "酒店星级",
            "hotelText": "酒店名称显示文本",
            "hotelUuid": "酒店UUID",
            "islandText": "岛屿名称显示文本",
            "islandUuid": "岛屿uuid",
            "memo": "酒店备注",
            "mixliveAmount": 66,
            "mixliveCurrencyId": 2,
            "mixliveCurrencyText": "混住费用币种符号显示文本",
            "name": "酒店价单名称",
            "position": 1,
            "purchaseType": 1,
            "supplierInfoId": 2,
            "supplierInfoText": "地接社供应商显示文本",
            "updateByText": "更改人显示文本",
            "updateDate": "2015-06-27",
            "updateDateString": "2015-06-27",
            "uuid": "酒店价单uuid"
        }

        bindBaseInfo(baseInfo);

        //酒店税金
        var hotelPlTaxPriceList = [
            {
                "chargeType": 1,
                "chargeTypeText": "收费类型显示文本",
                "currencyId": 2,
                "amount":66.0,
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "taxName": "政府税",
                "taxType": 1,
                "uuid": "酒店税金uuid"
            },
            {
                "chargeType": 1,
                "chargeTypeText": "收费类型显示文本",
                "currencyId": 2,
                "amount":66.0,
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "taxName": "服务税",
                "taxType": 2,
                "uuid": "酒店税金uuid"
            },
            {
                "chargeType": 1,
                "chargeTypeText": "收费类型显示文本",
                "currencyId": 2,
                "amount":66.0,
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "taxName": "服务税",
                "taxType": 2,
                "uuid": "酒店税金uuid"
            }
        ];
        bindTaxDictionary(hotelPlTaxPriceList);
        //税金例外
        var hotelPlTaxExceptionList = [
            {
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "exceptionName": "例外类型名称(1、房型；2、餐型；3、交通)",
                "exceptionType": 1,
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-23",
                "startDateString": "2015-06-23",
                "taxType": "1;2;3",
                "taxTypeText": "税费类型显示文本",
                "uuid": "1;3",
                "name": "游客类型显示文本",
                "uuid": "税金例外uuid"
            },{
                "endDate": "2015-06-22",
                "endDateString": "2015-06-22",
                "exceptionName": "例外类型名称(1、房型；2、餐型；3、交通)",
                "exceptionType": 1,
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-21",
                "startDateString": "2015-06-21",
                "taxType": "1;2",
                "taxTypeText": "税费类型显示文本",
                "uuid": "1;2;3;4;5",
                "name": "游客类型显示文本",
                "uuid": "税金例外uuid"
            },
            {
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "exceptionName": "例外类型名称(房型；餐型；交通)",
                "exceptionType": 2,
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "taxType": "1",
                "taxTypeText": "税费类型显示文本",
                "uuid":"1;2",
                "name": "游客类型显示文本",
                "uuid": "税金例外uuid"
            }
        ];
        bindTaxException(hotelPlTaxExceptionList);

        //酒店房型價格
        var roomPrices = {
            "酒店房型uuid1": [
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid1",
                    "hotelMealUuids": "餐型uuid1;餐型uuid2",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid1",
                    //价格类型（0、普通价；1、同行价）
                    "priceType": 0,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid3",
                    "hotelMealUuids": "餐型uuid1;餐型uuid2",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid1",
                    //价格类型（0、普通价；1、同行价）
                    "priceType": 1,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                },{
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-29",
                    "endDateString": "2015-06-29",
                    "hotelGuestTypeUuid": "酒店住客类型uuid3",
                    "hotelMealUuids": "餐型uuid1;餐型uuid2",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid1",
                    //价格类型（0、普通价；1、同行价）
                    "priceType": 1,
                    "startDate": "2015-06-29",
                    "startDateString": "2015-06-29",
                    "uuid": "酒店价单价格uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid3",
                    "hotelMealUuids": "餐型uuid3",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid1",
                    "priceType": 1,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                }
            ],
            "酒店房型uuid2": [
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid2",
                    "hotelMealUuids": "酒店餐型uuids",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid2",
                    "priceType": 0,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                }, {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid3",
                    "hotelMealUuids": "酒店餐型uuids",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid2",
                    "priceType": 1,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelGuestTypeUuid": "酒店住客类型uuid3",
                    "hotelMealUuids": "酒店餐型uuids",
                    "hotelPlUuid": "酒店价单uuid",
                    "hotelRoomText": "酒店房型显示文本",
                    "hotelRoomUuid": "酒店房型uuid2",
                    "priceType": 0,
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "uuid": "酒店价单价格uuid"
                }
            ]
        };
        var hotelPlRoomMemoList = [
            {
                "hotelPlUuid": "酒店价单uuid",
                "hotelRoomId": "酒店房型uuid1",
                "memo": "酒店价单房型价格 备注1",
                "uuid": "酒店房型价格备注uuid1"
            },
            {
                "hotelPlUuid": "酒店价单uuid",
                "hotelRoomId": "酒店房型uuid2",
                "memo": "酒店价单房型价格 备注2",
                "uuid": "酒店房型价格备注uuid2"
            }
        ]
        bindRoomTypePrices(roomPrices,hotelPlRoomMemoList);

        //交通费用
        var  islandWayList={
            "上岛方式uuid1": [
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种符号：￥、$",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelPlUuid": "酒店价单uuid",
                    "islandWay": "上岛方式uuid1",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "1",
                    "uuid": "交通费用uuid1"
                },
                {
                    "amount": 61,
                    "currencyId": 2,
                    "currencyMark": "币种符号：￥、$",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelPlUuid": "酒店价单uuid",
                    "islandWay": "上岛方式uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "2",
                    "uuid": "交通费用uuid2"
                }
            ],
            "上岛方式uuid2": [
                {
                    "amount": 12,
                    "currencyId": 2,
                    "currencyMark": "币种符号：￥、$",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelPlUuid": "酒店价单uuid",
                    "islandWay": "上岛方式uuid2",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "2",
                    "uuid": "交通费用uuid3"
                },
                {
                    "amount": 123,
                    "currencyId": 2,
                    "currencyMark": "币种符号：￥、$",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelPlUuid": "酒店价单uuid",
                    "islandWay": "上岛方式uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "3",
                    "uuid": "交通费用uuid4"
                }
            ]
        };
        var islandWayMemoList = [
            {
                "hotelPlUuid": "酒店价单uuid",
                "islandWay": "上岛方式uuid1",
                "memo": "酒店价单交通费用备注1",
                "uuid": "酒店上岛方式备注uuid1"
            },
            {
                "hotelPlUuid": "酒店价单uuid",
                "islandWay": "上岛方式uuid2",
                "memo": "酒店价单交通费用备注2",
                "uuid": "酒店上岛方式备注uuid2"
            }
        ]
        bindIslandWay(islandWayList,islandWayMemoList);
        //升餐费用
        var hotelRiseMealMap = {
            "酒店餐型uuid1": [
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelMealRiseText": "酒店升级餐型1",
                    "hotelMealRiseUuid": "酒店升级餐型uuid1",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "1",
                    "uuid": "酒店价单升餐uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelMealRiseText": "酒店升级餐型1",
                    "hotelMealRiseUuid": "酒店升级餐型uuid1",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "2",
                    "uuid": "酒店价单升餐uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-29",
                    "endDateString": "2015-06-29",
                    "hotelMealRiseText": "酒店升级餐型1",
                    "hotelMealRiseUuid": "酒店升级餐型uuid1",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-29",
                    "startDateString": "2015-06-29",
                    "travelerTypeUuid": "3",
                    "uuid": "酒店价单升餐uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-30",
                    "endDateString": "2015-06-30",
                    "hotelMealRiseText": "酒店升级餐型1",
                    "hotelMealRiseUuid": "酒店升级餐型uuid1",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-30",
                    "startDateString": "2015-06-30",
                    "travelerTypeUuid": "3",
                    "uuid": "酒店价单升餐uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelMealRiseText": "酒店升级餐型2",
                    "hotelMealRiseUuid": "酒店升级餐型uuid2",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "1",
                    "uuid": "酒店价单升餐uuid"
                }
            ],
            "酒店餐型uuid2": [
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelMealRiseText": "酒店升级餐型2",
                    "hotelMealRiseUuid": "酒店升级餐型uuid2",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "游客类型uuid",
                    "uuid": "酒店价单升餐uuid"
                },
                {
                    "amount": 66,
                    "currencyId": 2,
                    "currencyMark": "币种显示符号",
                    "endDate": "2015-06-28",
                    "endDateString": "2015-06-28",
                    "hotelMealRiseText": "酒店升级餐型2",
                    "hotelMealRiseUuid": "酒店升级餐型uuid2",
                    "hotelMealUuid": "酒店餐型uuid",
                    "hotelPlUuid": "酒店价单uuid",
                    "startDate": "2015-06-28",
                    "startDateString": "2015-06-28",
                    "travelerTypeUuid": "游客类型uuid",
                    "uuid": "酒店价单升餐uuid"
                }
            ]
        };
        var hotelPlRiseMealMemoList = [
            {
                "hotelMealUuid": "酒店餐型uuid1",
                "hotelPlUuid": "酒店价单uuid",
                "memo": "酒店价单升餐备注1",
                "uuid": "酒店价单升餐备注uuid"
            },
            {
                "hotelMealUuid": "酒店餐型uuid2",
                "hotelPlUuid": "酒店价单uuid",
                "memo": "酒店价单升餐备注2",
                "uuid": "酒店价单升餐备注uuid"
            }
        ];
        bindRiseMeal(hotelRiseMealMap,hotelPlRiseMealMemoList);

        //强制节日餐
        var hotelPlHolidayMealList = [
            {
                "amount": 66,
                "currencyId": 2,
                "currencyMark": "币种符号",
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "holidayMealName": "节日餐名称",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "travelerTypeUuid": "1",
                "uuid": "酒店价单节日餐uuid1"
            },
            {
                "amount": 66,
                "currencyId": 2,
                "currencyMark": "币种符号",
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "holidayMealName": "节日餐名称",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "travelerTypeUuid": "2",
                "uuid": "酒店价单节日s餐uuid2"
            },
            {
                "amount": 66,
                "currencyId": 2,
                "currencyMark": "币种符号",
                "endDate": "2015-06-27",
                "endDateString": "2015-06-27",
                "holidayMealName": "节日餐名称2",
                "hotelPlUuid": "酒店价单uuid",
                "startDate": "2015-06-27",
                "startDateString": "2015-06-27",
                "travelerTypeUuid": "3",
                "uuid": "酒店价单节日餐uuid3"
            }
        ];
        bindHolidayMeal(hotelPlHolidayMealList);

        var favorInfos={};
        bindPreferential(favorInfos);
        initCurrency();
    }

});
