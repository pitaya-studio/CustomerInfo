/**
 * Created by zhh on 2016/2/5.
 * 奢华-看板相关js
 */
/*=====S=====数据源信息*/
//游轮列表信息源
var cruiseships = [
  /*  {
    "uuid":"4718404aa7d647869f685412ae8dda62",//游轮UUID
    "name":"歌诗达邮轮-公主号"//游轮名称
    }*/
];

//船期列表数据源
var cruiseshipDates = [
   /* {
        cruiseshipStockUuid:"52223e0b3ff940999618870ca1760b52",//库存UUID
        shipDate:"2015-06-28",//船期
        cruiseshipCabinList:[
            {
                cruiseshipStockDetailUuid:"94f008f10c167cd8b336b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"f10c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"7cd8b33694f008f10c16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"336b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            },
            {
                cruiseshipStockDetailUuid:"11f008f10c167cd8b336b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"110c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"22d8b33694f008f10c16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"226b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            },
            {
                cruiseshipStockDetailUuid:"33f008f10c167cd8b336b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"330c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"44d8b33694f008f10c16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"446b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            },
            {
                cruiseshipStockDetailUuid:"55f008f10c167cd8b336b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"550c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"66d8b33694f008f10c16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"666b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            },
            {
                cruiseshipStockDetailUuid:"55f008f10c167cd8b336b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"550c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"77d8b33694f008f10c16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"776b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            }
        ]
    },
    {
        cruiseshipStockUuid:"f940999618870ca152223e0b3f760b52",//库存UUID
        shipDate:"2015-07-28",//船期
        cruiseshipCabinList:[
            {
                cruiseshipStockDetailUuid:"008f107cd8b33694fc16b97a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"f10c167cd8b94f008336b97a46f4bf3d",//舱型UUID
                cruiseshipCabinName:"SP",//名称
                stockAmount:50,//库存
                freePosition:30//余位
            },
            {
                cruiseshipStockDetailUuid:"c16b9008f107cd8b33694f7a46f4bf3d",//库存明细UUID
                cruiseshipCabinUuid:"336b97a46f4bf3df10c167cd8b94f008",//舱型UUID
                cruiseshipCabinName:"AP",//名称
                stockAmount:60,//库存
                freePosition:40//余位
            }
        ]
    }*/
];

var cruiseshipCabinList = [];

//产品列表数据源
var activetys = [
/*{
    "activityId":982, //产品ID
    "activityName":"四国",//产品名称
    "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
{
    "activityId":984, //产品ID
    "activityName":"三国",//产品名称
    "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
{
    "activityId":983, //产品ID
    "activityName":"二国",//产品名称
    "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":985, //产品ID
        "activityName":"五国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":986, //产品ID
        "activityName":"六国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":987, //产品ID
        "activityName":"七国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":988, //产品ID
        "activityName":"八国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":989, //产品ID
        "activityName":"九国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    },
    {
        "activityId":990, //产品ID
        "activityName":"十国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
    }*/
];

//下单人数据源
var creator = [
/*    {
        "createBy":982, //下单人ID
        "createByName":"张三"//下单人名称
    },
    {
        "createBy":855, //下单人ID
        "createByName":"李四"//下单人名称
    },
    {
        "createBy":983, //下单人ID
        "createByName":"王五"//下单人名称
    },
    {
        "createBy":999, //下单人ID
        "createByName":"良辰"//下单人名称
    }*/
];

//下单信息数据源
var salses = {
  /*  "982":[
    {
        "94f008f10c167cd8b336b97a46f4bf3d":[
            {
                "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                "activityId":982,//产品ID
                "departureCityName":"上海",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":983,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                "activityId":984,//产品ID
                "departureCityName":"上海",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":985,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                "activityId":986,//产品ID
                "departureCityName":"上海",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            }
            , {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":987,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":987,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":987,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":988,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":989,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":990,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":988,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":989,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                "activityId":990,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            }
        ]
    },{
        "7cd8b33694f008f10c16b97a46f4bf3d":[
            {
                "uuid":"36b97a46ff10c167cd8b94f00834bf3d",//库存订单UUID
                "activityId":982,//产品ID
                "departureCityName":"上海",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            },
            {
                "uuid":"6f4bf4f008336bf10c167cd8b997a43d",//库存订单UUID
                "activityId":983,//产品ID
                "departureCityName":"北京",//目的地名称
                "sex":"F",//性别（女：F，男：M）
                "fNum":2,//女人数
                "mNum":1,//男人数
                "fPiece":0, //	女拼（拼：0；不拼：1；）
                "mPiece":1 //	男拼（拼：0；不拼：1；）
            }
        ]
    }
],
    "855":[
        {
            "94f008f10c167cd8b336b97a46f4bf3d":[
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        },{
            "7cd8b33694f008f10c16b97a46f4bf3d":[
                {
                    "uuid":"36b97a46ff10c167cd8b94f00834bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"6f4bf4f008336bf10c167cd8b997a43d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        }
    ],
    "983":[
        {
            "94f008f10c167cd8b336b97a46f4bf3d":[
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":984,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":985,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":986,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
                , {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":988,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":989,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":990,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":988,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":989,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":990,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        },{
            "7cd8b33694f008f10c16b97a46f4bf3d":[
                {
                    "uuid":"36b97a46ff10c167cd8b94f00834bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"6f4bf4f008336bf10c167cd8b997a43d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        }
    ],
    "999":[
        {
            "94f008f10c167cd8b336b97a46f4bf3d":[
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":984,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":985,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"f10c167cd8b94f008336b97a46f4bf3d",//库存订单UUID
                    "activityId":986,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
                , {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":987,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":988,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":989,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":990,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":988,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":989,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"4f008336bf10c167cd8b997a46f4bf3d",//库存订单UUID
                    "activityId":990,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        },{
            "7cd8b33694f008f10c16b97a46f4bf3d":[
                {
                    "uuid":"36b97a46ff10c167cd8b94f00834bf3d",//库存订单UUID
                    "activityId":982,//产品ID
                    "departureCityName":"上海",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                },
                {
                    "uuid":"6f4bf4f008336bf10c167cd8b997a43d",//库存订单UUID
                    "activityId":983,//产品ID
                    "departureCityName":"北京",//目的地名称
                    "sex":"F",//性别（女：F，男：M）
                    "fNum":2,//女人数
                    "mNum":1,//男人数
                    "fPiece":0, //	女拼（拼：0；不拼：1；）
                    "mPiece":1 //	男拼（拼：0；不拼：1；）
                }
            ]
        }
    ]*/
}


var activetyColors = [{background:'#f3a497',borderColor:'#ee7b6f'},
{background:'#afb043',borderColor:'#7b7c38'},
{background:'#bd99c6',borderColor:'#aa79b3'},
{background:'#a07c7c',borderColor:'#734848'},
{background:'#89a07c',borderColor:'#6c7b63'},
{background:'#494e1f',borderColor:'#52524e'},
{background:'#4162a0',borderColor:'#485876'},
{background:'#844200',borderColor:'#5d4022'},
{background:'#218f71',borderColor:'#1f6955'},
{background:'#b43583',borderColor:'#8c4671'},
{background:'#97c0f3',borderColor:'#8da2bc'},
{background:'#9799f3',borderColor:'#7274b6'},
{background:'#f397ee',borderColor:'#b563b0'},
{background:'#8bbb3',borderColor:'#637c3a'},
{background:'#b67636',borderColor:'#7f5c3a'},
{background:'#d34709',borderColor:'#b85428'},
{background:'#00aaaa',borderColor:'#2f7676'},
{background:'#47cf35',borderColor:'#3c8932'},
{background:'#00a2ff',borderColor:'#308abd'},
{background:'#ffc600',borderColor:'#d6b02c'}];

var mdActivetyColors = [{background:'#f3a497',borderColor:'#ee7b6f'},
    {background:'#afb043',borderColor:'#7b7c38'},
    {background:'#bd99c6',borderColor:'#aa79b3'},
    {background:'#a07c7c',borderColor:'#734848'},
    {background:'#89a07c',borderColor:'#6c7b63'},
    {background:'#494e1f',borderColor:'#52524e'},
    {background:'#4162a0',borderColor:'#485876'},
    {background:'#844200',borderColor:'#5d4022'},
    {background:'#218f71',borderColor:'#1f6955'},
    {background:'#b43583',borderColor:'#8c4671'},
    {background:'#97c0f3',borderColor:'#8da2bc'},
    {background:'#9799f3',borderColor:'#7274b6'},
    {background:'#f397ee',borderColor:'#b563b0'},
    {background:'#8bbb3',borderColor:'#637c3a'},
    {background:'#b67636',borderColor:'#7f5c3a'},
    {background:'#d34709',borderColor:'#b85428'},
    {background:'#00aaaa',borderColor:'#2f7676'},
    {background:'#47cf35',borderColor:'#3c8932'},
    {background:'#00a2ff',borderColor:'#308abd'},
    {background:'#ffc600',borderColor:'#d6b02c'}];
//我的订单信息
var myOrderInfo = {
    /*cruiseshipCabin:[
        {
            cruiseshipCabinUuid:"f10c167cd8b94f008336b97a46f4bf3d",//舱型UUID
            cruiseshipCabinName:"SP"//名称
        },
        {
            cruiseshipCabinUuid:"336b97a46f4bf3df10c167cd8b94f008",//舱型UUID
            cruiseshipCabinName:"AP"//名称
        },
        {
            cruiseshipCabinUuid:"110c167cd8b94f008336b97a46f4bf3d",//舱型UUID
            cruiseshipCabinName:"SP"//名称
        },
        {
            cruiseshipCabinUuid:"226b97a46f4bf3df10c167cd8b94f008",//舱型UUID
            cruiseshipCabinName:"AP"//名称
        },
        {
            cruiseshipCabinUuid:"330c167cd8b94f008336b97a46f4bf3d",//舱型UUID
            cruiseshipCabinName:"SP"//名称
        },
        {
            cruiseshipCabinUuid:"446b97a46f4bf3df10c167cd8b94f008",//舱型UUID
            cruiseshipCabinName:"AP"//名称
        },
        {
            cruiseshipCabinUuid:"550c167cd8b94f008336b97a46f4bf3d",//舱型UUID
            cruiseshipCabinName:"SP"//名称
        },
        {
            cruiseshipCabinUuid:"666b97a46f4bf3df10c167cd8b94f008",//舱型UUID
            cruiseshipCabinName:"AP"//名称
        },
        {
            cruiseshipCabinUuid:"550c167cd8b94f008336b97a46f4bf3d",//舱型UUID
            cruiseshipCabinName:"SP"//名称
        },
        {
            cruiseshipCabinUuid:"776b97a46f4bf3df10c167cd8b94f008",//舱型UUID
            cruiseshipCabinName:"AP"//名称
        }
    ],
    activity:[
        {
        "activityId":982, //产品ID
        "activityName":"四国",//产品名称
        "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":984, //产品ID
            "activityName":"三国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":983, //产品ID
            "activityName":"二国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":985, //产品ID
            "activityName":"五国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":986, //产品ID
            "activityName":"六国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":987, //产品ID
            "activityName":"七国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":988, //产品ID
            "activityName":"八国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":989, //产品ID
            "activityName":"九国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        },
        {
            "activityId":990, //产品ID
            "activityName":"十国",//产品名称
            "activityType":1 //产品团期表类型（1：activitygroup表；）
        }
    ],
    depart:[
        {
            departureCityId : '2456',
            departureCityName : '北京'
        },
        {
            departureCityId : '2457',
            departureCityName : '上海'
        },
        {
            departureCityId : '2458',
            departureCityName : '武汉'
        },
        {
            departureCityId : '2459',
            departureCityName : '郑州'
        }
    ],
    stockOrderMaps:[
        {
            cruiseshipCabinUuid: '110c167cd8b94f008336b97a46f4bf3d',
            cruiseshipCabinName: 'AP',
            freePosition: 40,
            cruiseshipStockDetailUuid: '11f008f10c167cd8b336b97a46f4bf3d',
            stockOrders: [{
            	uuid: '6f4bf4f008336bf10c167cd8b997a43d',//订单uuid
                activityId: '982',
                activityName: '四国',
                fNum: '10',
                fPiece: '0',
                mNum: '10',
                mPiece: '1',
                departureCityId: '2456',
                departureCityName: '北京'
            }, {
            	uuid: '114bf4f008336bf10c167cd8b997a43d',//cruiseship_stock_order.UUID
                activityId: '983',
                activityName: '三国',
                fNum: '10',
                fPiece: '0',
                mNum: '10',
                mPiece: '1',
                departureCityId: '2456',
                departureCityName: '北京'
            }]
        },
        {
            uuid : 'df4bf4f008336bf10c167cd8b997a43d',//cruiseship_stock_order.UUID
            cruiseshipCabinUuid : '220c167cd8b94f008336b97a46f4bf3d',
            cruiseshipCabinName : 'SP',
            freePosition : 30,
            cruiseshipStockDetailUuid : '22f008f10c167cd8b336b97a46f4bf3d',
            stockOrders: [{
                activityId: '982',
                activityName: '四国',
                fNum: '10',
                fPiece: '0',
                mNum: '10',
                mPiece: '1',
                departureCityId: '2456',
                departureCityName: '上海'
            }, {
                activityId: '983',
                activityName: '三国',
                fNum: '10',
                fPiece: '0',
                mNum: '10',
                mPiece: '1',
                departureCityId: '2456',
                departureCityName: '上海'
            }]
        }
    ]*/
}

/*=====E=====数据源信息*/

var borardScrollResize;

(function($){
    $.infoWindow = function(opts){
        var defaults = {};
        defaults = {
            //获取window选择器
            winSelector:"#infoPop",
            //提示信息选择器
            textInfoSelector:'[name="infoText"]',
            //提示信息
            infoText:"",
            //提示时间ms
            time:2000,
            icon:0//成功0，失败1，警告2
        };
        opts = $.extend(defaults,opts);
        var $win = $(opts.winSelector);
        $(opts.textInfoSelector,$win).text(opts.infoText);
        $win.css('margin-left','-'+$win.width()/2+'px');
        //TODO 实现警告
        if(opts.icon===0){
        	$win.removeClass('fail');
        }else{
        	$win.addClass('fail');
        }
        $win.show();
        setTimeout(function(){
            $win.hide();
        },opts.time);
    };
	
	$.confirmWindow = function(opts){
	var defaults={};
	defaults = {
	//获取window选择器
	winSelector:"#confirmPop",
	//confirm按钮选择器
	confirmBtnSelector:"#confirmOperate",
	//cancel按钮选择器
	cancelBtnSelector:"#cancelOperate",
	//提示信息选择器
	textInfoSelector:"#confirmInfo",
	//提示信息
	textInfo:"",
	maskLv:0,
	submit:function(v){
			return true;
		}
	}
	opts = $.extend(defaults,opts);
	var $win = $(opts.winSelector);
	var event = {
	_closeWin:function(){
	hideMask(opts.maskLv);
	$win.fadeOut(300);
	},
	_openWin:function(){
		$(opts.textInfoSelector,$win).text(opts.textInfo);
	showMask(opts.maskLv);
	$win.fadeIn(300);
	}
	}
	event._openWin();
	$('[name="closePop"]').off().on('click',function(){
		event._closeWin();
	})
	$(opts.confirmBtnSelector,$win).off().on('click',function(){
	if(opts.submit(1)!==false){
	event._closeWin();
	}
	});
	$(opts.cancelBtnSelector,$win).off().on('click',function(){
	if(opts.submit(0)!==false){
	event._closeWin();
	}
	});
	}
	})(jQuery);

$(function(){
    renderBoard();
});
//渲染面板
function renderBoard(){
    //初始化滚动条,渲染数据时应调用scrollResize();
    renderCruiseships();
    borardScrollResize = tableScroll();
}

//初始化游轮列表
function renderCruiseships(){
    $.ajax({
        url:$ctx+'/cruiseshipStock/queryCruiseshipInfos',
        type:'post',
        async:false,
        success:function(data){
        	cruiseships  = jQuery.parseJSON(data.data);
            if(cruiseships.length===0){
                return ;
            }
            var $ul = $('#cruiseships');
            $ul.children().remove();
            for(var i= 0,len=cruiseships.length;i<len;i++){
                var cruiseship = cruiseships[i];
                $('<li></li>').text(cruiseship.name).attr('uuid',cruiseship.uuid).appendTo($ul);
            }
            BoxScroll($ul);
            //默认显示船期最近的游轮，后端传来的数据的第一个
            $('#cruiseship').text(cruiseships[0].name).attr('uuid',cruiseships[0].uuid);
            renderCruiseshipDates();
        }
    });
}

//渲染船期列表
function renderCruiseshipDates(){
    $.ajax({
        url:$ctx+'/cruiseshipStock/getShipDatesByShipInfoUuid',
        type:'post',
        async:false,
        data: {cruiseshipInfoUuid :$('#cruiseship').attr('uuid')},
        success:function(data){
        	cruiseshipDates = jQuery.parseJSON(data.data);
            var $ul = $('#cruiseshipDates');
            $ul.children().remove();
            if(cruiseshipDates.length == 0) {
            	return ;
            }
            for(var i= 0,len=cruiseshipDates.length;i<len;i++){
                var cruiseshipDate = cruiseshipDates[i];
                $('<li></li>').text(cruiseshipDate.shipDate).attr('uuid',cruiseshipDate.cruiseshipStockUuid).appendTo($ul);
            }
            BoxScroll($ul);
            //默认显示最近的船期，后端传来的数据的第一个
            $('#cruiseshipDate').text(cruiseshipDates[0].shipDate).attr('uuid',cruiseshipDates[0].cruiseshipStockUuid);
            cruiseshipCabinList = cruiseshipDates[0].cruiseshipCabinList;
            renderCruiseshipCabin();
            renderCreator();
            renderProduct();
            if(borardScrollResize){
                borardScrollResize();
            }
        }
    });
}

//渲染舱型信息
function renderCruiseshipCabin(){
    var $table = $('#cabinTypes');
    var $htr = $('#cabinTypes table thead tr');
    $htr.children().remove();
    var $btr = $('#cabinTypes table tbody tr');
    $btr.children().remove();
    for(var i= 0,len=cruiseshipCabinList.length;i<len;i++){
        $htr.append('<th></th>');
        var cruiseshipCabin = cruiseshipCabinList[i];
        var td = '<td><div class="ullage" cruiseshipStockDetailUuid='+cruiseshipCabin.cruiseshipStockDetailUuid+'>' +
            '<p>'+cruiseshipCabin.cruiseshipCabinName+'</p>' +
            '<p><em>'+cruiseshipCabin.freePosition+'/</em><em>'+cruiseshipCabin.stockAmount+'</em>' +
            '</p></div></td>'
        $btr.append(td);
    }
}
//渲染销售信息
function renderCreator(){
    $.ajax({
        url:$ctx+'/cruiseshipStock/queryCreateUsersByStockUuid',
        type:'post',
        data:{
            cruiseshipStockUuid:$('#cruiseshipDate').attr('uuid')
        },
        async:false,
        success:function(data){
        	creator = jQuery.parseJSON(data.data);
            var $creatorUl = $('#sales ul:first');
            $creatorUl.children().remove();
            for(var i= 0,len=creator.length;i<len;i++){
                var $li = $('<li></li>').attr('creatorId',creator[i].createBy).appendTo($creatorUl);
                $li.append('<div class="container-sales-nale"><div class="subwrap-name"><div class="content-name">'+creator[i].createByName+'</div></div></div>');
            }
        }
    });
}


//渲染关联产品
function renderProduct(){
    $.ajax({
        url:$ctx+'/cruiseshipStock/queryRelProducts',
        type:'post',
        data:{
            cruiseshipStockUuid:$('#cruiseshipDate').attr('uuid'),
            queryStatus:0    //未关联的产品显示不出来，预留一个-1状态为显示全部产品
        },
        async:false,
        success:function(data){
        	activetys = jQuery.parseJSON(data.data);
            var $activetysUl = $('#activetyListArea ul');
            $activetysUl.children('li:gt(0)').remove();
            for(var i = 0,len = activetys.length;i<len;i++){
                var activety = activetys[i];
                var activetyColor = activetyColors[i];
                //将产品id赋值给颜色数组,渲染表格时使用
                activetyColor.activityId = activety.activityId;
                var html = '<li>' +
                    '<em class="country_01" style="background:'+activetyColor.background+';border:'+activetyColor.borderColor+'"></em>' +
                    "[<label>"+ activety.fromArea +"</label>]" +
                    "<span> " +
                    "<span class=\"checkbox-made marrten active\" name=\"activeCheck\"></span> " +
                    "<div class=\"product-name\" activityId="+activety.activityId+" activityType="+activety.activityType+">"+activety.activityName+"</div> " +
                    "</span></li>";
                $activetysUl.append(html);
            }
            renderSale();
        }
    });
}

//渲染下单数据
function renderSale(){
	
	var datas = {};
	datas.cruiseshipStockUuid = $('#cruiseshipDate').attr('uuid');
	datas.activityList =  getSelectedProduct();
    $.ajax({
        url:$ctx+'/cruiseshipStock/queryStockOrderInfos',
        type:'post',
        data:getPostData(datas),
        async:false,
        success:function(data){
        	salses = jQuery.parseJSON(data);
            //列数
            var cols = cruiseshipCabinList.length;
            var rows = creator.length===0?1:creator.length;
            var $table = $('#saleStatus table:first');
            var $theadRow = $table.find('tr:first');
            //移除之前的表格数据
            $theadRow.children().remove();
            $table.children('tbody').remove();
            for(var i=0;i<cols;i++){
                $('<th></th>').appendTo($theadRow);
            }
            for(var i= 0;i<rows;i++){
                var $dataArea = $('<tbody>').appendTo($table);
                var $dataRow = $('<tr></tr>').appendTo($dataArea);
                //此处判断是否为最后一行且销售应大于2,用于判断详细信息的展示方式
                var isLast = false;
                if(i==rows-1&&i>1){
                    isLast = true;
                }
                for(var j=0;j<cols;j++){
                    var $td = $('<td></td>').appendTo($dataRow);
                    renderCell(j,i,$td,isLast);
                }
            }
        }
    });
}
//渲染单元格
function renderCell(x,y,$td,isLast){
    //获取库存明细uuid
    var cruiseshipstockdetailuuid = $('#cabinTypes').find('.ullage').eq(x).attr('cruiseshipstockdetailuuid');
    //获取下单人ID
    var creatorid = $('#sales li').eq(y).attr('creatorid');
    //根据库存明细和下单人信息获取对应的下单信息
    var salesInfo = getOrderListKb(cruiseshipstockdetailuuid,creatorid);
    //增加鼠标移入溢出事件
    var $li = $('<li onmouseover="hoverOn(this)" onmouseout="hoverOut(this)" creatorid='+creatorid+' cruiseshipstockdetailuuid='+cruiseshipstockdetailuuid+'></li>').appendTo($td);
    if(!salesInfo){
        return ;
    }
    var $tmp = $('[name="foolishWay"]');
    var isOverFlow = false;
    for(var i= 0,len=salesInfo.length;i<len;i++){
        var saleInfo = salesInfo[i];
        var activetyColor = getObjectByProp(activetyColors,'activityId',saleInfo.activityId);
        var $div = $('<div></div>',{
            class:'country'
        }).css('background',activetyColor.background);
        var html = '';
        if(saleInfo.mNum){
            html += '<span><em>'+saleInfo.mNum+'</em><i>M</i>'+(saleInfo.mPiece==0?"<span class=\"tips-together\"></span>":"")+'</span>'
        }
        if(saleInfo.mNum&&saleInfo.fNum){
        	html += '+';
        }else{
        	html += '&nbsp;';
        }
        if(saleInfo.fNum){
            html += '<span><em>'+saleInfo.fNum+'</em><i>F</i>'+(saleInfo.fPiece==0?"<span class=\"tips-together\"></span>":"")+'</span>&nbsp;'
        }
        html += '<span>'+saleInfo.departureCityName+'</span>';
        $div.append(html);
        $tmp.append($div.clone());
        if($tmp.height()<=108){
            $li.append($div);
        }else if($tmp.height()>108){
            isOverFlow = true;
        }
    }
    if(isOverFlow){
        $li.append('<div class="view-all-pro-minification" name="saleDetail" isLast='+isLast+'></div>');
        var $mirrorDiv = $('<div></div>').append($tmp.clone().children()).addClass(isLast?'view-all-prolist-bottom':'view-all-prolist');
        //var $mirrorDiv = $tmp.clone().attr('name','').addClass(isLast?'view-all-prolist-bottom':'view-all-prolist');
        $li.append($mirrorDiv);
        BoxScroll($mirrorDiv);
    }
    $tmp.children().remove();
}



function getOrderListKb(cruiseshipstockdetailuuid,creatorid){
    var creatorSales = salses[creatorid];
    if(!creatorSales){
    	return null;
    }
    for(var i= 0,len=creatorSales.length;i<len;i++){
        var salesInfo = creatorSales[i][cruiseshipstockdetailuuid];
        if(salesInfo){
            return salesInfo;
        }
    }
    return null;
}

//获取选中的产品信息
function getSelectedProduct(){
    var selectedProducts = [];
    $('#activetyListArea [name="activeCheck"].active').each(function(){
        var $this = $(this);
        var selectedProduct = {
            activityId:$this.next().attr('activityId'),
            activityType:$this.next().attr('activityType')
        }
        selectedProducts.push(selectedProduct);
    });
    return selectedProducts;
}


function tableScroll(){
    var colHeaderScroll = $('#cabinTypes').niceScroll({
        autohidemode:'leave',
        cursorcolor: "#d1d1d1",
        cursoropacitymax: 0.5,
        cursorwidth: "7px",
        cursorborder: "0",
        cursorborderradius: "3px",
        boxzoom: false,
        enablemousewheel: false
    });
    var rowHeaderScroll = $('#sales').niceScroll({
        autohidemode:'leave',
        cursorcolor: "#d1d1d1",
        cursoropacitymax: 0.5,
        cursorwidth: "7px",
        cursorborder: "0",
        cursorborderradius: "3px",
        boxzoom: false,
        enablemousewheel: false
    });
    var contentScroll = BoxScroll($('#saleStatus'));
    colHeaderScroll.hide();
    rowHeaderScroll.hide();
    $('#saleStatus').scroll(function(){
        var $this = $(this);
        $('#cabinTypes').scrollLeft($this.scrollLeft());
        $('#sales').scrollTop($this.scrollTop());
        colHeaderScroll.resize();
        rowHeaderScroll.resize();
    });
    var activetyAreaScroll = BoxScroll($('#activetyListArea'));
    return function(){
        colHeaderScroll.resize();
        rowHeaderScroll.resize();
        contentScroll.resize();
        activetyAreaScroll.resize();
    }
}

////展开收起游轮
//$(document).on('click','#epCruiseships',function(){
//    var $ul = $('#cruiseships');
//    if($ul.is(':hidden')){
//        $ul.slideDown(200);
//    }else{
//        $ul.slideUp(200);
//    }
//});
//
////产开收起船期
//$(document).on('click','#epCruiseshipDates',function(){
//    var $ul = $('#cruiseshipDates');
//    if($ul.is(':hidden')){
//        $ul.slideDown(200);
//    }else{
//        $ul.slideUp(200);
//    }
//});

$(document).on('click','[name="slideList"]',function(e){
    e.stopPropagation();
    var $ul = $(this).children('ul');
    var isHidden = $ul.is(':hidden');
    //先隐藏所有的下拉框
    $('[name="slideList"] ul').slideUp(200);
    if(isHidden){
        $ul.slideDown(200);
    }
});

$(document).on('click',function(){
    $('[name="slideList"] ul').slideUp(200);
});
//打开筛选条件
$(document).on('click','.pull-down-own',function(e){
	e.stopPropagation();
    var $ul = $(this).next().children('ul');
    $ul.slideDown(200);
});
//关闭筛选条件
$(document).on('mouseleave','[name="filterUl"]',function(){
    $(this).slideUp(200);
});

//选中触发筛选
$(document).on('click','[name="filterUl"] em',function(){
    var $this = $(this);
    if($this.hasClass('active')){
        $(this).removeClass('active');
    }else{
        $(this).addClass('active');
    }
    reSearchMyOrder();
});
//依照现有条件重新查询我的订单列表
function reSearchMyOrder(){
    var cruiseshipCabinUuids = getSelectedFilter('#cabin');
    var activityIds = getSelectedFilter('#product-sel');
    var departureCityIds = getSelectedFilter('#start');
    renderMDList(cruiseshipCabinUuids,activityIds,departureCityIds,true);
}

//获取筛选条件
function getSelectedFilter(selector){
    var uuids = [];
    $(selector).find('li').each(function(){
    	if($(this).children().hasClass('active')){
            uuids.push($(this).attr('uuid'));
    	}
    });
    return uuids;
}


//选择游轮
$(document).on('click','#cruiseships li',function(){
    var $this = $(this);
    $('#cruiseship').text($this.text()).attr('uuid',$this.attr('uuid'));
    renderCruiseshipDates();
});
//选择船期
$(document).on('click','#cruiseshipDates li',function(){
    var $this = $(this);
    $('#cruiseshipDate').text($this.text()).attr('uuid',$this.attr('uuid'));
    cruiseshipCabinList = getObjectByProp(cruiseshipDates,'cruiseshipStockUuid',$this.attr('uuid')).cruiseshipCabinList;
    renderCruiseshipCabin();
    renderCreator();
    renderProduct();
});
//全选
$(document).on('click','#checkAll',function(){
    var $this = $(this);
    if($this.hasClass('active')){
        $this.removeClass('active');
        $this.parent().
            siblings().find('[name="activeCheck"]').
            removeClass('active');
    }else{
        $this.addClass('active');
        $this.parent().
            siblings().find('[name="activeCheck"]').
            addClass('active');
    }
})
//产品选中事件
$(document).on('click','[name="activeCheck"]',function(){
    var $this = $(this);

    if($this.hasClass('active')){
        $this.removeClass('active');
        $('#checkAll').removeClass('active');
    }else{
        $this.addClass('active');
        if($('#activetyListArea [name="activeCheck"]:not(.active)').length==0){
            $('#checkAll').addClass('active');
        }
    }
});

//刷新
$(document).on('click','#refreshBoard',function(){
    renderBoard();
});
//按产品筛选
$(document).on('click','#filterByActivety',function(){
    renderSale();
});
//显示详情
$(document).on('click','[name="saleDetail"]',function(){
    var $this = $(this);
    var $detail = $this.next();
    $detail.off().on('mouseleave',function(){
        $detail.hide(200);
    });
    $detail.show(200);
});
//打开我的订单
$(document).on('click','#myOrder',function(){
    showMask();
    //查询我的订单下游轮列表
    renderMDCruiseships();
    $('#myOrderPop').fadeIn(500);
    $('#myOrderPop').find('#saveMD').removeClass('active');
});

//渲染我的订单下的游轮列表
function renderMDCruiseships(){
    $('#mdCruiseship').attr('uuid','').text('');
    $('#mdCruiseships').children().remove();
    $.ajax({
        url:$ctx+'/cruiseshipStock/queryCruiseshipInfos',
        type:'post',
        async:false,
        success:function(data){
//        	   data = cruiseships;
        	   cruiseships  = jQuery.parseJSON(data.data);
               if(cruiseships.length===0){
                   return ;
               }
               var $ul = $('#mdCruiseships');
               cruiseships.unshift({"uuid":'',"name":'请选择'});
               for(var i= 0,len=cruiseships.length;i<len;i++){
                   var cruiseship = cruiseships[i];
                   $('<li></li>').text(cruiseship.name).attr('title',cruiseship.name).attr('uuid',cruiseship.uuid).appendTo($ul);
               }
               BoxScroll($ul);
               //默认显示船期最近的游轮，后端传来的数据的第一个
               $('#mdCruiseship').text(cruiseships[0].name).attr('uuid',cruiseships[0].uuid);
               renderMDCruiseshipDates();
        },
        error:function(data){
        }
    })
}
//查询我的订单下的船期
function renderMDCruiseshipDates(){
    $('#mdCruiseshipDate').attr('uuid','').text('');
    $('#mdCruiseshipDates').children().remove();
    if($('#mdCruiseship').attr('uuid')==""){
    	cruiseshipDates.unshift({" cruiseshipStockUuid":'',"shipDate":'请选择'});
    	 //默认显示最近的船期，后端传来的数据的第一个
        $('#mdCruiseshipDate').text(cruiseshipDates[0].shipDate).attr('uuid',cruiseshipDates[0].cruiseshipStockUuid);
        renderMDList();
    	return;
     }
    $.ajax({
        url:$ctx+'/cruiseshipStock/getShipDatesByShipInfoUuid',
        type:'post',
        async:false,
        data:{
        	"cruiseshipInfoUuid" :$('#mdCruiseship').attr('uuid')
        },
        success:function(data){
        	cruiseshipDates = jQuery.parseJSON(data.data);
//        	$('#mdCruiseshipDates').children().remove();
            var $ul = $('#mdCruiseshipDates');
            for(var i= 0,len=cruiseshipDates.length;i<len;i++){
                var cruiseshipDate = cruiseshipDates[i];
                $('<li></li>').text(cruiseshipDate.shipDate).attr('uuid',cruiseshipDate.cruiseshipStockUuid).appendTo($ul);
            }
            BoxScroll($ul);
            //默认显示最近的船期，后端传来的数据的第一个
            $('#mdCruiseshipDate').text(cruiseshipDates[0].shipDate).attr('uuid',cruiseshipDates[0].cruiseshipStockUuid);
            renderMDList();
        },
        error:function(data){
        }
    });
}
//渲染订单列表
function renderMDList(cruiseshipCabinUuids,activityIds,departureCityIds,notFilter){
	var datas = {};
    var $table = $('#mdOrderTable');
    $table.children('tbody').remove();
	datas.cruiseshipUuid = $('#mdCruiseship').attr('uuid');
	datas.cruiseshipStockUuid=$('#mdCruiseshipDate').attr('uuid');
	datas.cruiseshipCabinUuid=cruiseshipCabinUuids;
	datas.activityId=activityIds;
	datas.departureCityId=departureCityIds;
    $.ajax({
        url:$ctx+'/cruiseshipStockOrder/list',
        type:'post',
        async:false,
        data:getPostData(datas),
        success:function(data){
        	myOrderInfo=jQuery.parseJSON(data);
        	createOrdertable();
            if(notFilter){
                return ;
            }
            //清除舱型，产品，出发地的过滤条件
            $table.find('.copySelect li').remove();
            createCabinList(myOrderInfo.cruiseshipCabin);
            createProductList(myOrderInfo.activity);
            createStartList(myOrderInfo.depart);
        },
        error:function(data){
        }
    });
}

function createCabinList(cruiseshipCabins){
    var $ul = $('#cabin ul');
    for(var i= 0,len=cruiseshipCabins.length;i<len;i++){
        var cruiseshipCabin = cruiseshipCabins[i];
        $ul.append('<li uuid='+cruiseshipCabin.cruiseshipCabinUuid+'><em class="checkbox-made marrten "></em><label for="">'+cruiseshipCabin.cruiseshipCabinName +'</label></li>');
    }
    BoxScroll($ul);
}

function createProductList(activitys){
    var $ul = $('#product-sel ul');
    for(var i= 0,len=activitys.length;i<len;i++){
        var activity = activitys[i];
        $ul.append('<li uuid='+activity.activityId +'><em class="checkbox-made marrten "></em><label for="">'+activity.activityName+'</label></li>');
    }
    BoxScroll($ul);
}

function createStartList(departs){
    var $ul = $('#start ul');
    for(var i= 0,len=departs.length;i<len;i++){
        var depart = departs[i];
	        $ul.append('<li uuid='+depart.departureCityId  +'><em class="checkbox-made marrten "></em><label for="">'+depart.departureCityName +'</label></li>');
    }
    BoxScroll($ul);
}

function createOrdertable(){
    var $table = $('#mdOrderTable');
    var $tbody = $('<tbody></tbody>').appendTo($table);
    for(var i = 0,len = myOrderInfo.activity.length;i<len;i++) {
        var activety = myOrderInfo.activity[i];
        var activetyColor = mdActivetyColors[i];
        //将产品id赋值给颜色数组,渲染订单列表时使用
        activetyColor.activityId = activety.activityId;
    }
    for(var i= 0,len=myOrderInfo.stockOrderMaps.length;i<len;i++){
        var orderInfo = myOrderInfo.stockOrderMaps[i];
        var stockOrders = orderInfo.stockOrders;
        var rowspan = stockOrders.length==0?1:stockOrders.length;
        var $tr = $('<tr></tr>').appendTo($tbody);
        var html = '<td rowspan='+rowspan+' title='+orderInfo.cruiseshipCabinName+'>'+ orderInfo.cruiseshipCabinName  +'</td><td rowspan='+rowspan+'>'+orderInfo.freePosition +'</td>';
        var firstRow = stockOrders[0];
        if(!firstRow){
            firstRow = {
                activityId: '',
                activityName: '',
                fNum: '',
                fPiece: '1',
                mNum: '',
                mPiece: '1',
                departureCityId: '',
                departureCityName: ''
            };
        }
        html += createOrderRow(firstRow);
        $tr.append(html);
        for(var j= 1,length=stockOrders.length;j<length;j++){
            var $tr1 = $('<tr></tr>').appendTo($tbody);
            $tr1.append(createOrderRow(stockOrders[j]));
        }
    }
    BoxScroll($table.parent());
}

//生成订单中的一对多信息
function createOrderRow(stockOrder){
    var activetyColor = getObjectByProp(mdActivetyColors,'activityId',stockOrder.activityId);
    var background = activetyColor?activetyColor.background:'#000000';
    var html = '<td uuid="'+stockOrder.uuid+'" name="activityIndo"><span style="color:'+background+'">'+stockOrder.activityName+'</span></td>';
    html += '<td><div class="copySelect number"><span>'+stockOrder.fNum+'</span><input class="number-count-in" type="text" name="fNum" style="display: none;color:white;" data-type="number"  value='+stockOrder.fNum+' ></div>';
    if(stockOrder.fPiece==0){
        html += '<em class="checkbox-made active" name="isFRoomPool"></em>';
    }else{
        html += '<em class="checkbox-made" name="isFRoomPool"></em>';
    }
    html += '</td><td><div class="copySelect number"><span>'+stockOrder.mNum+'</span><input class="number-count-in"  type="text" name="mNum" style="display: none;color:white;" data-type="number" value='+stockOrder.mNum+' ></div>';
    if(stockOrder.mPiece==0){
        html += '<em class="checkbox-made active" name="isMRoomPool"></em>';
    }else{
        html += '<em class="checkbox-made" name="isMRoomPool"></em>';
    }
    html += '</td><td>'+stockOrder.departureCityName+'</td>';
    html += '<td><div class="operator"><i name="deleteOrderInfo"></i><em name="modifyCruiseNum"></em></div></td>';
    return html;
}

//我的订单修改按钮
$(document).on('click','[name="modifyCruiseNum"]',function(){
    var $this = $(this);
    var $tr = $this.parents('tr:first');
    $tr.find('input').show().prev().hide();
    //为复选框绑定点击事件
    $tr.find('[name="isFRoomPool"],[name="isMRoomPool"]').on('click',function(){
        var $this = $(this);
        if($this.hasClass('active')){
            $this.removeClass('active');
        }else{
            $this.addClass('active');
        }
    });
    $this.addClass('disabled');
    $('#saveMD').addClass('active');
});
//删除我的订单
var deleteMDOrder = function(obj){
	var uuid= $(obj).parent().parent().prev().prev().prev().prev().attr("uuid");
	var fnum=$(obj).parent().parent().prev().prev().prev().find("span").text();
	var mnum=$(obj).parent().parent().prev().prev().find("span").text();
	$.ajax({
		type:"POST",
		url:$ctx+"/cruiseshipStockOrder/delete",
		data:{
			"uuids":uuid,
			"fnum":fnum,
			"mnum":mnum
		},
	success:function(data){
//		top.$.jBox.tip('删除成功','success');
//		$.jBox.InfoWindow.open('删除成功',1);
		$.infoWindow({
			'infoText':"删除成功"
		});
		renderMDCruiseships();
		renderBoard();
		}
	});
}

//我的订单删除按钮
$(document).on('click','[name="deleteOrderInfo"]',function(){
	var _this = this;
    $.confirmWindow(
    	    {textInfo:"您确定删除该条订单?",maskLv:1,
    	        submit:function(v){
    	            if(v===1){
    	            	deleteMDOrder(_this);
    	                return true;
    	            }
    	        }
    	    });
});


//关闭删除提示
$(document).on('click','#cancelDelete,#closeDelInfo',function(){
    hideMask(1);
    $('#deleteOrderInfo').fadeOut(300);
});


//关闭我的订单窗口
$(document).on('click','#closeMyOrderPop',function(){
    hideMask();
    $('#myOrderPop').hide();
});

$(document).on('click','#cancelMD',function(){
    hideMask();
    $('#myOrderPop').hide();
});

//保存报名
$(document).on('click','#saveBM',function(){
	if(!$(this).hasClass('active')){
		return ;
	}
	var datas = [];
	var data = {};
	data.activityId = $("#popActivetySelected").attr("uuid");
	data.cruiseshipStockUuid =  $("#popCruiseshipDate").attr("uuid");
	data.cruiseshipStockDetailUuid = $("#popCabinSelected").attr("cruiseshipstockdetailuuid");
	data.cruiseshipCabinName = $("#popCabinSelected").text();
	data.fnum = $("#fnum").val();
	data.mnum = $("#mnum").val();
	//拼0 不拼1
	if($("#fpiece").attr("checked")){
		data.fpiece = $("#fpiece").val();
	}else{
		data.fpiece = "1";
	}
	
	if($("#mpiece").attr("checked")){
		data.mpiece = $("#mpiece").val();
	}else{
		data.mpiece = "1";
	}
	
	datas.push(data);
	$.ajax({
        url:$ctx+'/cruiseshipStockOrder/makeOrder',
        type:'post',
        data:getPostData(datas),
        async:false,
        cache:false,
        success:function(data){
        	if(data==1){
        		$.infoWindow({
        			'infoText':"占位成功"
        		});
            	$('#cruiseshipApplyPop').hide();
            	hideMask(100)
            	renderBoard();
        	}else{
        		$.infoWindow({
        			'infoText':"余位不足，保存失败！",
        			 icon:1
        		});
        	}
        }
	});
});


//保存我的订单
$(document).on('click','#saveMD',function(){
	if(!$(this).hasClass('active')){
		return ;
	}
    var orderInfos = [];
    //获取所有的隐藏修改按钮
    $('#myOrderPop').find('[name="modifyCruiseNum"].disabled').each(function(){
        var $this = $(this);
        var $tr = $this.parents('tr:first');
        var orderInfo = {
            orderUuid:$tr.find('[name="activityIndo"]').attr('uuid'),
            fNum:$tr.find('[name="fNum"]').val(),
            mNum:$tr.find('[name="mNum"]').val(),
            fPiece:$tr.find('[name="isFRoomPool"]').hasClass('active')?0:1,
            mPiece:$tr.find('[name="isMRoomPool"]').hasClass('active')?0:1
        };
        orderInfos.push(orderInfo);
    });
    $.ajax({
        url:$ctx+'/cruiseshipStockOrder/update',
        type:'post',
        async:false,
        data:getPostData(orderInfos),
        success:function(data){
        	if(data=="2"){
        		$.infoWindow({
        			'infoText':"保存成功"
        		});
            	renderMDCruiseships();
            	renderBoard();
            	$('#saveMD').removeClass('active');
        	}else{
        		$.infoWindow({
        			'infoText':"保存失败，舱型名称为"+data+"的余位不足，请重新输入！",
        			 icon:1
        		});
        	}
        }
    });
});


//我的订单中选择游轮
$(document).on('click','#mdCruiseships li',function(){
    var $this = $(this);
    $('#mdCruiseship').text($this.text()).attr('uuid',$this.attr('uuid'));
    renderMDCruiseshipDates();
    $this.parent().slideUp(200);
});
//我的订单中选择船期
$(document).on('click','#mdCruiseshipDates li',function(){
    var $this = $(this);
    $('#mdCruiseshipDate').text($this.text()).attr('uuid',$this.attr('uuid'));
    renderMDList();
    $this.parent().slideUp(200);
});

//打开报名
$(document).on('click','#cruiseshipApply',function(){
    showMask();
    var $pop = $('#cruiseshipApplyPop').show();
    var $cruiseship = $('#cruiseship');
    var $cruiseshipDate = $('#cruiseshipDate');
    //赋值游轮
    $('#popCruiseship').attr('uuid',$cruiseship.attr('uuid')).text($cruiseship.text());
    //赋值船期
    $('#popCruiseshipDate').attr('uuid',$cruiseshipDate.attr('uuid')).text($cruiseshipDate.text());
    //赋值产品列表
    //判断
	$.ajax({
        url:$ctx+'/cruiseshipStock/cruiseshipStockGroupRelByActivityId',
        type:'post',
        async:false,
		data:{
            activityid:activetys[0]?activetys[0].activityId:'',
            stockUuid :$cruiseshipDate.attr('uuid')
        },
        success:function(data){
        	$('#popActivetySelected').attr('isConnect',data);
        }
    });
	//清空信息
    $pop.find('#fnum').val('');
    $pop.find('#mnum').val('');
    $pop.find('#fpiece').removeAttr('checked');
    $pop.find('#mpiece').removeAttr('checked');
    $('#saveBM').removeClass('active');
	
	
    //判断
    $('#popActivetySelected').attr('uuid',activetys[0]?activetys[0].activityId:'').text(activetys[0]?activetys[0].activityName:'');
    var selected = $('#popActivetySelected').text();
    $('#popActivetySelected').attr('title',selected);
    
    var $popActivetyUl = $('#popActivetySelect');
    $popActivetyUl.children().remove();
    for(var i= 0,len=activetys.length;i<len;i++){
    	$popActivetyUl.append('<li activityid='+activetys[i].activityId+' activitytype='+activetys[i].activityType+' title='+activetys[i].activityName+'>'+activetys[i].activityName+'</li>')
    }
    
    BoxScroll($popActivetyUl);
    //赋值舱型列表
    var $popCabinSelect = $('#popCabinSelect');
    if(cruiseshipCabinList[0]){
        //父级下拉框加库存明细uuid 当前舱行属于哪个库存明细
        $('#popCabinSelected').attr('cruiseshipStockDetailUuid',cruiseshipCabinList[0].cruiseshipStockDetailUuid).attr('uuid',cruiseshipCabinList[0].cruiseshipCabinUuid).text(cruiseshipCabinList[0].cruiseshipCabinName);
    }
    $popCabinSelect.children().remove();
    for(var i= 0,len=cruiseshipCabinList.length;i<len;i++){
    $popCabinSelect.append('<li uuid='+cruiseshipCabinList[i].cruiseshipCabinUuid+' cruiseshipStockDetailUuid='+cruiseshipCabinList[i].cruiseshipStockDetailUuid+'>'+cruiseshipCabinList[i].cruiseshipCabinName+'</li>')
    }
    BoxScroll($popCabinSelect);
    
    
});

//选择产品
$(document).on('click','#popActivetySelect li',function(){
	
	//cruiseshipStockGroupRelByActivityId    返回0未关联
	
    var $this = $(this);
	$.ajax({
        url:$ctx+'/cruiseshipStock/cruiseshipStockGroupRelByActivityId',
        type:'post',
        async:false,
		data:{
            activityid:$this.attr('activityid'),
            stockUuid : $('#cruiseshipDate').attr('uuid')
        },
        success:function(data){
        	$('#popActivetySelected').attr('isConnect',data);
        }
    });

    $('#popActivetySelected').text($this.text()).attr('uuid',$this.attr('activityid'));
    var selected = $('#popActivetySelected').text();
    $('#popActivetySelected').attr('title',selected);
    

});
//选择舱型
$(document).on('click','#popCabinSelect li',function(){
    var $this = $(this);
    $('#popCabinSelected').text($this.text()).attr('uuid',$this.attr('uuid')).attr('cruiseshipStockDetailUuid',$this.attr('cruiseshipStockDetailUuid'));
});

//关闭报名窗口
$(document).on('click','#closeApplyPop',function(){
    hideMask();
    var $pop = $('#cruiseshipApplyPop');
    $pop.hide();
});

//添加遮罩
function showMask(lv){
    if(lv==1){
        $('#sec-mask').show();
    }else{
        $('#fir-mask').show();
    }

}

$(document).on('click','.plate-set',function(){
    var $this = $(this);
    if($this.attr('isShow')){
        $this.parent().animate({right:'-1060px'},800);
        $this.removeAttr('isShow');
    }else{
        $this.parent().animate({right:'0'},800);
        $this.attr('isShow',true);
    }

});

//移除遮罩
function hideMask(lv){
    if(lv==1){
        $('#sec-mask').hide();
    }else{
        $('#fir-mask').hide();
    }
}

$(document).on('keyup','#fnum,#mnum',function(){
	var isConnect = $('#popActivetySelected').attr('isConnect');
	var hasData = $('#fnum').val()||$('#mnum').val();
	if(hasData!=''&&isConnect!=0){
		$("#saveBM").addClass("active");
	}else{
		$("#saveBM").removeClass("active");
	}
});

//鼠标移入事件，为了控制行列的状态切换
function hoverOn(obj){
	var creatorId = $(obj).attr('creatorid');
	var cruiseshipstockdetailuuid = $(obj).attr('cruiseshipstockdetailuuid');
	$('#sales').find('li[creatorid="'+creatorId+'"]').addClass('active');
	$('#cabinTypes').find('div[cruiseshipstockdetailuuid='+cruiseshipstockdetailuuid+']').addClass('active');
	$(obj).parent().siblings('td').each(function(){
		$(this).children(':first').addClass('active');
	})
}
//鼠标移入事件，为了控制行列的状态切换
function hoverOut(obj){
	var creatorId = $(obj).attr('creatorid');
	var cruiseshipstockdetailuuid = $(obj).attr('cruiseshipstockdetailuuid');
	$('#sales').find('li[creatorid="'+creatorId+'"]').removeClass('active');
	$('#cabinTypes').find('div[cruiseshipstockdetailuuid='+cruiseshipstockdetailuuid+']').removeClass('active');
	$(obj).parent().siblings('td').each(function(){
		$(this).children(':first').removeClass('active');
	})
}
