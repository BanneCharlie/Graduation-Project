package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-03-02 14:29:47
 */
@Data
@TableName("business_product_data_ledger")
public class ProDataLedger {

    @Excel(name = "设备档案号", sort = 1)
    @TableField(value = "equipmentArchiveNumber")
    private String equipmentArchiveNumber;
    @Excel(name = "任务单编号", sort = 2)
    @TableField(value = "workOrderNumber")
    private String workOrderNumber;
    @Excel(name = "报告编号", sort = 3)
    @TableField(value = "reportNumber")
    private String reportNumber;
    @Excel(name = "流程状态", sort = 4)
    @TableField(value = "processStatus")
    private String processStatus;
    @Excel(name = "设备所属监察机构", sort = 5)
    @TableField(value = "supervisoryOrgan")
    private String supervisoryOrgan;
    @Excel(name = "设备所属定期检验单位", sort = 6)
    @TableField(value = "regularInspectionUnit")
    private String regularInspectionUnit;
    @Excel(name = "监督检验单位", sort = 7)
    @TableField(value = "supervisionInspectionUnits")
    private String supervisionInspectionUnits;
    @Excel(name = "使用单位", sort = 8)
    @TableField(value = "useUnit")
    private String useUnit;
    @Excel(name = "注册代码", sort = 9)
    @TableField(value = "registrationCode")
    private String registrationCode;
    @Excel(name = "设备代码", sort = 10)
    @TableField(value = "deviceCode")
    private String deviceCode;
    @Excel(name = "使用证号", sort = 11)
    @TableField(value = "useCertificateNumber")
    private String useCertificateNumber;
    @Excel(name = "设备类别", sort = 12)
    @TableField(value = "deviceClass")
    private String deviceClass;
    @Excel(name = "设备品种", sort = 13)
    @TableField(value = "equipmentVarieties")
    private String equipmentVarieties;
    @Excel(name = "设备名称", sort = 14)
    @TableField(value = "deviceName")
    private String deviceName;
    @Excel(name = "设备级别", sort = 15)
    @TableField(value = "equipmentLevel")
    private String equipmentLevel;
    @Excel(name = "安全状况等级", sort = 16)
    @TableField(value = "safetyStatusLevel")
    private String safetyStatusLevel;
    @Excel(name = "投用日期", sort = 17)
    @TableField(value = "dateOfUse")
    private String dateOfUse;
    @Excel(name = "使用单位应急救援电话", sort = 18)
    @TableField(value = "useEmergencyRescueTelephonNumberOfTheUnit")
    private String useEmergencyRescueTelephonNumberOfTheUnit;
    @Excel(name = "场所性质", sort = 19)
    @TableField(value = "placeNature")
    private String placeNature;
    @Excel(name = "出厂编号", sort = 20)
    @TableField(value = "manufacturingNumber")
    private String manufacturingNumber;
    @Excel(name = "制造日期", sort = 21)
    @TableField(value = "makeDate")
    private String makeDate;
    @Excel(name = "维保单位系统ID", sort = 22)
    @TableField(value = "maintenanceUnitSystemId")
    private String maintenanceUnitSystemId;
    @Excel(name = "维保单位", sort = 23)
    @TableField(value = "maintenanceUnit")
    private String maintenanceUnit;
    @Excel(name = "检验科室", sort = 24)
    @TableField(value = "inspectionDepartment")
    private String inspectionDepartment;
    @Excel(name = "检验类型", sort = 25)
    @TableField(value = "inspectionType")
    private String inspectionType;
    @Excel(name = "检验日期", sort = 26)
    @TableField(value = "examinationDate")
    private String examinationDate;
    @Excel(name = "报告模板", sort = 27)
    @TableField(value = "reportTemplate")
    private String reportTemplate;
    @Excel(name = "下次检验日期", sort = 30)
    @TableField(value = "nextInspectionDate")
    private String nextInspectionDate;
    @Excel(name = "检验结论", sort = 31)
    @TableField(value = "inspectTheConclusion")
    private String inspectTheConclusion;
    @Excel(name = "责任检验员", sort = 32)
    @TableField(value = "responsibilityInspector")
    private String responsibilityInspector;
    @Excel(name = "检验员1", sort = 33)
    @TableField(value = "inspectorOne")
    private String inspectorOne;
    @Excel(name = "检验员2", sort = 34)
    @TableField(value = "inspectorTwo")
    private String inspectorTwo;
    @Excel(name = "检验员3", sort = 35)
    @TableField(value = "inspectorThree")
    private String inspectorThree;
    @Excel(name = "检验员4", sort = 35)
    @TableField(value = "inspectorFour")
    private String inspectorFour;
    @Excel(name = "录入日期", sort = 36)
    @TableField(value = "dateOfEntry")
    private String dateOfEntry;
    @Excel(name = "录入员", sort = 37)
    @TableField(value = "dataEntryStaff")
    private String dataEntryStaff;
    @Excel(name = "自审日期", sort = 38)
    @TableField(value = "selfExaminationDate")
    private String selfExaminationDate;
    @Excel(name = "自审人员", sort = 39)
    @TableField(value = "selfExaminationPersonnel")
    private String selfExaminationPersonnel;
    @Excel(name = "审核日期", sort = 40)
    @TableField(value = "auditDate")
    private String auditDate;
    @Excel(name = "审核人员", sort = 41)
    @TableField(value = "auditPersonnel")
    private String auditPersonnel;
    @Excel(name = "批准日期", sort = 42)
    @TableField(value = "dateOfApproval")
    private String dateOfApproval;
    @Excel(name = "批准人员", sort = 43)
    @TableField(value = "approvedPersonnel")
    private String approvedPersonnel;
    @Excel(name = "打印日期", sort = 44)
    @TableField(value = "printDate")
    private String printDate;
    @Excel(name = "打印人员", sort = 45)
    @TableField(value = "printPersonnel")
    private String printPersonnel;
    @Excel(name = "一般整改项数量", sort = 46)
    @TableField(value = "sameCount")
    private String sameCount;
    @Excel(name = "重要整改项数量", sort = 47)
    @TableField(value = "importantCount")
    private String importantCount;
    @Excel(name = "是否案例", sort = 48)
    @TableField(value = "whetherTheCase")
    private String whetherTheCase;
    @Excel(name = "是否复检")
    @TableField(value = "whetherReinspection")
    private String whetherReinspection;
    @Excel(name = "报告文件名")
    @TableField(value = "reportFileName")
    private String reportFileName;
    @Excel(name = "已签章")
    @TableField(value = "signedAndSealed")
    private String signedAndSealed;
    @Excel(name = "财务审核")
    @TableField(value = "financeExamine")
    private String financeExamine;
    @Excel(name = "交费确认")
    @TableField(value = "paymentConfirm")
    private String paymentConfirm;
    @Excel(name = "A类项不合格数量")
    @TableField(value = "numberOfUnqualifiedOne")
    private String numberOfUnqualifiedOne;
    @Excel(name = "B类项不合格数量")
    @TableField(value = "numberOfUnqualifiedTwo")
    private String numberOfUnqualifiedTwo;
    @Excel(name = "C类项不合格数量")
    @TableField(value = "numberOfUnqualifiedThree")
    private String numberOfUnqualifiedThree;
    @Excel(name = "邮件发送状态")
    @TableField(value = "mailSendingStatus")
    private String mailSendingStatus;
    @Excel(name = "制造单位")
    @TableField(value = "manufacturingUnit")
    private String manufacturingUnit;
    @Excel(name = "规格型号")
    @TableField(value = "specificationsAndModels")
    private String specificationsAndModels;
    @Excel(name = "归档编号")
    @TableField(value = "archiveNumber")
    private String archiveNumber;
    @Excel(name = "厂车牌照号码")
    @TableField(value = "factoryLicensePlateAccordingToNumber")
    private String factoryLicensePlateAccordingToNumber;
    @Excel(name = "厂车底盘编号")
    @TableField(value = "factoryChassisNumber")
    private String factoryChassisNumber;
    @Excel(name = "厂车发动机编号")
    @TableField(value = "factoryCarEngineNumber")
    private String factoryCarEngineNumber;
    @Excel(name = "电梯曳引机编号")
    @TableField(value = "elevatorTractorMachineNumber")
    private String elevatorTractorMachineNumber;
    @Excel(name = "报告类型")
    @TableField(value = "reportType")
    private Integer reportType;

}
