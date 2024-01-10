// 格式化时间
const parseTime = function(timeStr) {
    const [date, time] = timeStr.split(" ");
    const [year, month, day] = date.split("-");
    const [hour, minute] = time.split("：");
    return new Date(year, month - 1, day, hour, minute);
};
// 根据开始时间或结束时间，给当前文字标红
const SignTime = function(listArr) {
	const now = new Date(); // 当前时间
	
	listArr.forEach(obj => {
	   const time = parseTime(obj.startTime);
	   
	   const timeGap = time.getTime() - now.getTime();
	  if (timeGap <= 2 * 60 * 60 * 1000 && timeGap > 0) {
	    obj.fontColor = 1; // 时间在当前时间的两小时以内，添加数字1
	  } else if (time < now) {
	    obj.fontColor = 2; // 时间超过当前时间，添加数字2
	  } else {
	    obj.fontColor = 0; // 其他情况，添加数字0
	  }
	});
	
	return listArr;
}
// 根据发送时间，删减时间
const deleteTime = function(listArr) {
    const today = new Date();
	listArr.forEach(item => {
	  const itemTime = parseTime(item.apply_time);
	  
	  // 判断是否为当天
	  if (itemTime.toDateString() === today.toDateString()) {
	    // 是当天，修改时间格式
	    const hours = itemTime.getHours();
	    const minutes = itemTime.getMinutes();
	    item.apply_time = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
	  } else {
	    // 不是当天，修改时间格式
	    const month = itemTime.getMonth() + 1; // 月份从0开始计算，所以要加1
	    const day = itemTime.getDate();
	    item.apply_time = `${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
	  }
	});
	return listArr
};

// 根据发送时间，排序时间
const sortTime = function(time) {
	return time.sort((a, b) => parseTime(b.apply_time) - parseTime(a.apply_time));
};
// 根据开始时间，排序时间
const sortSatrtTime = function(time) {
	return time.sort((a, b) => parseTime(b.startTime) - parseTime(a.startTime));
};

// 按时间排序的比较函数
const compareTime = function(a, b) {
  // 将“周一、周二”等转换为相应数字
  const weekdays = ["周一", "周二", "周三", "周四", "周五", "周六", "周日"];
  const aWeekday = weekdays.indexOf(a.start.slice(0, 2));
  const bWeekday = weekdays.indexOf(b.start.slice(0, 2));

  // 判断星期的顺序
  if (aWeekday < bWeekday) {
    return -1;
  } else if (aWeekday > bWeekday) {
    return 1;
  } else {
    // 将时间转换为小时和分钟，再进行比较
    const aTime = parseInt(a.start.slice(3, 5)) * 60 + parseInt(a.start.slice(6, 8));
    const bTime = parseInt(b.start.slice(3, 5)) * 60 + parseInt(b.start.slice(6, 8));

    if (aTime < bTime) {
      return -1;
    } else if (aTime > bTime) {
      return 1;
    } else {
      return 0;
    }
  }
};

// 日期改为星期
const DataToWeek = function(tableData) {
	const daysOfWeek = ["周一", "周二", "周三", "周四", "周五", "周六", "周日"];
	
	const updatedData = tableData.map(item => {
	    if (item.week.includes("-")) {
	        const date = new Date(item.week);
	        const dayOfWeek = daysOfWeek[date.getDay()];
	        
	        item.start = item.start.replace(item.week, dayOfWeek);
	        item.end = item.end.replace(item.week, dayOfWeek);
			item.week = dayOfWeek;
	    }
	    return item;
	});
	
	return updatedData;
};

// export的作用是导出函数，formatTime是vue调用formatTime()函数的接口
export default {
	SignTime,
	sortTime,
	sortSatrtTime,
	deleteTime,
	compareTime,
	DataToWeek
}