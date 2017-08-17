
var travelRequirements = {
		/**
		 * 询价接待要求表单json数据，通过这些数据生成询价接待表单
		 * @author lihua.xu
		 * @时间 2014年9月28日
		 * @type jsonArray
		 */
		formData : [
				{name:"团队类型",code:"travelTeamType",isExistOther:true,bigClass:"notUsed",class1:"lonely",options:{option_1:{name:"单独成团",value:1,class1:"lonely"},option_2:{name:"参加拼团",value:2,class1:"lonely"}}},
				{name:"护照类型",code:"visaType",isExistOther:true,bigClass:"top",options:{option_1:{name:"因私护照",value:1},option_2:{name:"因公护照",value:2}}},
//				{name:"护照类型",code:"visaType",isExistOther:true,bigClass:"top",class1:"lonely",options:{option_1:{name:"因私护照",value:1,class1:"lonely"},option_2:{name:"因公护照",value:2,class1:"lonely"}}},
				{name:"酒店要求",code:"hotelType",isExistOther:true,bigClass:"hotelRequire",options:{option_1:{name:"三星",value:1},option_2:{name:"四星",value:2},option_3:{name:"五星",value:3}}},
				{name:"酒店位置",code:"hotelPosition",isExistOther:true,bigClass:"hotelRequire",options:{option_1:{name:"郊区",value:1},option_2:{name:"市郊",value:2},option_3:{name:"市区 ",value:3},option_4:{name:"市中心 ",value:4}}},
				{name:"房间要求",code:"roomType",isExistOther:true,bigClass:"hotelRequire",options:{option_2:{name:"双标间",value:2},option_1:{name:"单人间",value:1},option_3:{name:"三人间",value:3}}},
				{name:"用车要求",code:"carType",isExistOther:true,bigClass:"trafficRequire",optionsType:2,options:{
								option_1:{name:"9座车",value:1},option_2:{name:"中巴",value:2},option_3:{name:"大车 ",value:3,info:{sufname:"座 ",type:"text","class":"seach_shortinput"}},option_4:{name:"不用车 ",value:4}
								}
				},				
				{name:"导游要求",code:"guideTyp",isExistOther:true,bigClass:"guideRequire",class1:"lonely",options:{option_1:{name:"司机兼导游",value:1,class1:"lonely"},option_2:{name:"一司一导",value:2,class1:"lonely"}}},
				{name:"领队要求",code:"leaderType",isExistOther:true,bigClass:"guideRequire",class1:"lonely",options:{option_1:{name:"需要领队",value:1,class1:"lonely"},option_2:{name:"不需要领队",value:2,class1:"lonely"}}},
				{name:"景点要求",code:"attractionType",isExistOther:true,bigClass:"viewRequire",class1:"lonely",options:{option_1:{name:"门票含讲解",value:1,class1:"lonely"},option_2:{name:"其他门票",value:2,class1:"lonely"}}},
				{name:"公务活动",code:"publicWordType",isExistOther:true,bigClass:"businessRequire",class1:"lonely",optionsType:2,options:{
								option_1:{name:"无公务活动",value:1,class1:"lonely"},option_2:{name:"有公务活动",value:2,class1:"lonely",info:{prevname:"安排城市： ",type:"text"}}
								}
				},
				{name:"早餐要求 ",code:"breakfastType",isExistOther:true,bigClass:"foodRequire",options:{option_1:{name:"欧陆自助",value:1},option_2:{name:"美式自助",value:2}}},
				{name:"正餐要求 ",code:"dinnerType",isExistOther:true,bigClass:"foodRequire",options:{option_1:{name:"五菜一汤",value:1},option_2:{name:"特色菜",value:2}}},
				{name:"签证要求 ",code:"visaNeedType",bigClass:"top",options:{option_1:{name:"需办理",value:1,info:{prevname:"备注：",sufname:"",type:"text","class":"seach_shortinput"}}}}
		],

		/**
		 * 询价接待要求表单json数据大类 对应formData中的bigClass
		 * @author yue.wang
		 * @时间 2014年12月04日
		 * @type jsonArray
		 */
		bigClass : [
		    {name:"",code:"top"},
			{name:"·酒店要求（多选）",code:"hotelRequire"},
			{name:"·交通要求（多选）",code:"trafficRequire"},
			{name:"·导游领队（单选）",code:"guideRequire"},
			{name:"·景点要求（单选）",code:"viewRequire"},
			{name:"·公务要求（单选）",code:"businessRequire"},
			{name:"·餐饮要求（多选）",code:"foodRequire"}
		]
};

