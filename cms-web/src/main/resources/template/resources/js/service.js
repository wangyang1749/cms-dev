var url = location.hostname;
var protocol = window.location.protocol;
const service = axios.create({
    baseURL: protocol+"//"+url+":8080",
    timeout: 10000,
    withCredentials: true,
})





const articleApi = {}
articleApi.list = (categortId, page) => {
    return service({
        url: `/option/categoryAjax/${categortId}`,
        params: { page: page },
        method: 'get'
    })
}
articleApi.getVisit=(id)=>{
    return service({
        url: `/option/visit/${id}`,
        method: 'get'
    })
}


const commentApi={}
commentApi.add=(data_)=>{
    return service({
        url:'/api/comment',
        method:'post',
        data:data_
    })
}