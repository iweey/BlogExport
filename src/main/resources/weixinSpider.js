var url = require('url');
var http = require('http');
var querystring = require('querystring');

module.exports = {
    summary: '微信公众号爬虫',
    *beforeSendResponse(requestDetail, responseDetail) {
        var link = requestDetail.url;

        // 第一页
        if (/mp\/profile_ext\?action=home/i.test(link)) {
            console.log(link)

            // 根据返回的数据状态组装相应的自动滚动加载JS
            var autoNextScrollJS = getAutoNextScrollJS();

            // 修改返回的body内容，插入JS
            var newResponse = Object.assign({}, responseDetail.response);
            newResponse.body += autoNextScrollJS;
            return {
                response: newResponse
            };
        }

        // 第二页以后
        if (/mp\/profile_ext\?action=getmsg/i.test(link)) {
            console.log(link)
            weixin(link);
            return;
        }

        return null;
    }
};

function weixin(link) {
    var post_data = querystring.stringify({
        link: link,
    });
    var options = {
        host: '127.0.0.1',
        port: 8080,
        path: '/weixin',
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Content-Length': post_data.length
        }
    };
    var req = http.request(options, function (serverFeedback) {
        if (serverFeedback.statusCode == 200) {
            // ignore
            console.log("请求成功")
        } else {
            console.log("请求失败，请查看异常信息");
        }
    });
    req.write(post_data);
    req.end();
}

function getAutoNextScrollJS() {
    var nextJS = '';
    nextJS += '<script type="text/javascript">';
    nextJS += ' setTimeout(\'window.scrollTo(0,document.body.scrollHeight)\', 1000);';
    nextJS += '<\/script>';
    return nextJS;
}

