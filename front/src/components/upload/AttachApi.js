import request from 'ecip-web/utils/request'

export const findAttachByBizIed = (data, url) => {
  const postReq = {
    url: url || 'api/v1/attach/list',
    method: 'post',
    data: data
  }
  return request(postReq)
}
