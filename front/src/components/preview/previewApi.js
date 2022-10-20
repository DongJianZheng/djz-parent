import request from '@/utils/request'

export const checkPreview = (params, data) => {
  return request({
    url: '/api/v1/preview/checkPreview',
    method: 'get',
    params: params
  })
}
