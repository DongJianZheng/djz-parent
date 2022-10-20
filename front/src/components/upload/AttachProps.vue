<script>
export default {
  props: {
    impUrl: { //  后台上传url
      type: String,
      default: 'api/v1/attach/upload',
      required: false
    },
    tplUrl: { //  模板下载url ，多个模版使用数组 [{name:'',url:''}]
      type: String | Array,
      required: false
    },
    name: { //  默认上传表单的名字和id，多实例需要唯一
      type: String,
      default: 'file',
      required: false
    },
    accept: { //  表单的accept属性, MIME type,image/png,image/gif,image/jpeg,image/webp
      type: String,
      required: false
    },
    extensions: { //  允许上传的文件后缀,jpg,gif,png,webp
      type: Array | String | RegExp,
      required: false
    },
    size: { // 允许上传的最大字节，最大上传 50M
      type: Number,
      required: false,
      default: 1024 * 1024 * 50
    },
    timeout: { // 上传超时时间毫秒
      type: Number,
      required: false,
      default: 0
    },
    maximum: { // 列表最大文件数
      type: Number,
      required: false,
      default: 0
    },
    thread: { // 同时并发上传的文件数量 线程数
      type: Number,
      required: false,
      default: 1
    },
    multiple: { //  支持选择多个文件
      type: Boolean,
      required: false
    },
    drop: { //  拖拽上传
      type: Boolean,
      default: true,
      required: false
    },
    canDel: { //  是否可以操作删除
      type: Boolean,
      default: true,
      required: false
    },
    canPreview: { //  是否可以预览
      type: Boolean,
      default: true,
      required: false
    },
    canUpload: { //  是否支持上传
      type: Boolean,
      default: true,
      required: false
    },
    showColumnStatus: { //  是否显示上传状态列
      type: Boolean,
      default: true,
      required: false
    },
    showColumnFileType: { //  是否显示上传状态列
      type: Boolean,
      default: true,
      required: false
    },
    showColumnExecutePhase: { //  是否显示执行阶段列
      type: Boolean,
      default: false,
      required: false
    },
    showColumnCheckbox: { //  是否显示复选框
      type: Boolean,
      default: false,
      required: false
    },
    canDownload: { //  是否支持下载
      type: Boolean,
      default: false,
      required: false
    },
    disableOption: { //  资料类别禁止选择
      type: Boolean,
      default: false,
      required: false
    },
    isFilterOption: { //  资料类别自动过滤
      type: Boolean,
      default: false,
      required: false
    },
    showTips: { //  是否展示提醒
      type: Boolean,
      default: false,
      required: false
    },
    tipsContent: { //  提醒内容
      type: String,
      required: false
    },
    rowSelectShow: { //  是否支行文件重新选择
      type: Boolean,
      default: false,
      required: false
    },
    data: { // 其它参数
      type: Object,
      default: () => {
        return {}
      },
      required: false
    },
    queryParam: { // 查询参数
      type: Object,
      default: () => {
        return {}
      },
      required: false
    },
    isReturnId: { //  上传成功后，是否只返回附件ID
      type: Boolean,
      default: true,
      required: false
    },
    singleReturn: { //  是否单个上传成功后直接返回
      type: Boolean,
      default: false,
      required: false
    },
    mountedQuery: { //  首次是否查询数据
      type: Boolean,
      default: true,
      required: false
    },
    trackParam: { // 跟踪参数
      type: [Number, String, Object],
      required: false
    },
    model: {
      type: String,
      default: 'table', // 支持tab 和 table 模式
      required: false
    },
    bizId: { // 业务Id，提供此首次会返回业务关联的附件；上传新附件会自动设置业务关联
      type: String,
      required: false
    },
    bizIdOnlyQuery: { // 控制bizId 使用用于上传时自动关联
      type: Boolean,
      default: false,
      required: false
    },
    bizType: { // 业务分类，提供此值，则自动关联附件资料类型进行匹配，参考字典：SP_BIZ_TYPE
      type: String,
      required: false
    },
    projectStage: { // 项目阶段，参考字典：SP_BIZ_PROJECT_STAGE
      type: String,
      required: false
    },
    fileType: { // 制定文件的类型
      type: String,
      required: false
    },
    subFileType: { // 同一个fileType 下的 文件子类别
      type: String,
      required: false
    },
    remark: { // 附件备注
      type: String,
      required: false
    },
    executePhase: { // 执行阶段
      type: Number,
      required: false
    },
    dataType: { // 数据类型: 默认：1 主业务数据，2 历史数据
      type: Number,
      required: false,
      default: 1
    },
    projectId: { // 项目ID，提供此会自动关联到项目
      type: String,
      required: false
    },
    bizIds: { // 业务Id集合，用于默认查询
      type: Array,
      required: false
    },
    reqSeq: {
      type: Number,
      default: 0,
      required: false
    },
    typeMaximum: {
      type: Array,
      required: false,
      default: () => {
        return [
          //   { label: '会议纪要文件', fileType: 2010, required: true, maximum: 1 }, // 其它字段，subFileType、executePhase也支持
          //   { label: '其它', fileType: 2099, required: false, maximum: 1 }
        ]
      }
    },
    queryUrl: {
      type: String,
      required: false
    },
    dataFilter: {
      type: Function,
      required: false
    },
    required: { // 是否一定校验一定要上传附件，false：没有附件也有回调，true：必须上传附件才会有回调
      type: Boolean,
      required: false,
      default: true
    }
  }
}
</script>
