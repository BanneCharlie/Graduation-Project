
// 在月份、日期、小时等小于10前面补0
function padDate(value) {
  return value < 10 ? '0' + value : value
}
// 获取当前系统日期 并格式化
export function getCurrentTime() {
  var date = new Date()
  var year = date.getFullYear()
  var month = padDate(date.getMonth() + 1)
  var day = padDate(date.getDate())
  var hours = padDate(date.getHours())
  var minutes = padDate(date.getMinutes())
  var seconds = padDate(date.getSeconds())
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
}
// 格式化输入的日期
export function formatData(input) {
  var d = new Date(input);
  var year = d.getFullYear();
  var month = d.getMonth() < 9 ? "0" + (d.getMonth() + 1) : "" + (d.getMonth() + 1);
  var day = d.getDate() < 10 ? "0" + d.getDate() : "" + d.getDate();
  var hour = d.getHours() < 10 ? "0" + d.getHours() : "" + d.getHours();
  var minutes = d.getMinutes() < 10 ? "0" + d.getMinutes() : "" + d.getMinutes();
  var seconds = d.getSeconds() < 10 ? "0" + d.getSeconds() : "" + d.getSeconds();
  return (year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds);
}
