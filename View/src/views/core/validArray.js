export const validArray = {
    // 强制条件
    requireValue:{ required: true, message: '该项不能为空', trigger: 'blur' },
    requireValueSelect:{required: true, message: '您还未选择该项内容', trigger: 'change' },
    // 输入字符长度条件
    inputChartLengthMin:{ min: 6, message: '不能低于6个字符', trigger: 'blur' },
    inputChartLengthBetween:{ min: 3, max: 6, message: '长度在 3 到 6 个字符', trigger: 'blur' },
    inputChartLengthMax:{ max: 12, message: '不能超过12个字符', trigger: 'blur' },
    inputPhoneNumberLength:
    {
        validator: function(rule, value, callback) {
        if (/^1[3-9]\d{9}$/.test(value) == false) {
            callback(new Error("手机号格式错误"));
        } else {
            callback();
        }
        },
        trigger: "blur"
    },
    inputZipCodeLength:{ min: 6, max: 6, message: '请检查邮编格式', trigger: 'blur' },
    // 检验类型
    numberValue: { type: 'number', message: '该项必须为数值类型',trigger: 'blur'},
    // 必须为正数
    signlessNumber:{
        validator(rule,value,callback){
          if(Number.isInteger(Number(value)) && Number(value) >= 0){
            callback();
          }else{
            callback(new Error("请输入有效数字"));
          }
        },
        trigger: 'blur',
      },
    dateValue:{ type: 'date', required: true, message: '请选择日期', trigger: 'change' },
    timeValue:{ type: 'date', required: true, message: '请选择时间', trigger: 'change' },
    emailValue:{ type: 'email', message: '请检查您的邮箱格式', trigger: ['blur', 'change'] },
}

export default{
    validArray
}
