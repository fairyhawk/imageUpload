imageUpload
===========
图片上传功能
<pre><code>
kindeditor中使用，解决跨域
kindeditor4.1.10
kindeditor3.5

</code></pre>

<pre><code>
跨域解决方法从a.com提交图片到b.com，b.con的action处理图片的上传和返回：
1）.原方式：a.com的action处理上传图片，kind会读取json格式,直接返回json格式如：
{"error":0,"url":"/upload/demo_web/temp/20140510/1399698690364609649.jpg"}即可
2）.b.com处理时，的不同点

2.1：b.com的action返回
//加参数s解释在后面
http://a.com/kindeditor/plugins/image/redirect.html?s={"error":0,"url":"/upload/sns/temp/20140510/1399702049464462863.jpg"}#{"error":0,"url":"/upload/sns/temp/20140510/1399702049464462863.jpg"}
kindeditor中原始的为：
http://a.com/kindeditor/plugins/image/redirect.html#{"error":0,"url":"/upload/sns/temp/20140510/1399702049464462863.jpg"}

2.2：a.com下的 http://a.com/kindeditor/plugins/image/redirect.htm内容：

*********************
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ie shit</title>
<script type="text/javascript">
	    function getParameter(val) {
	    	var uri = window.location.search;
	    	var re = new RegExp("" + val + "=([^&?]*)", "ig");
	    	return ((uri.match(re)) ? (uri.match(re)[0].substr(val.length + 1)) : null);
	    }
    
        var upload_callback = function(){
        	var data=getParameter("s");
        	var location_hash=location.hash;
        	//ie6取不到hash???使用url传参数s
        	if(location_hash!=null && location_hash!="" && location_hash){
        		 var data = location.hash ? location.hash.substring(1) : '';
                 document.getElementsByTagName("body")[0].innerHTML = '<pre>' + data  + '</pre>';
        	}else{
        		 var data=getParameter("s");
        		 document.getElementsByTagName("body")[0].innerHTML = '<pre>' + data  + '</pre>';
        	}
        };
    </script>
</head>
<body onload="upload_callback();">
</body>
</html>
*************************************
2.3：上面2部分完成了跨域可以用使用了。测试通过 IE6,7,8,9,10,火狐，谷歌，
苹果safari下有问题：修改a.com中kindeditor-all.js中
修改KUploadButton方法 4136-4225行之间，
找到处理json字符的地方：
var doc = K.iframeDoc(iframe),
				pre = doc.getElementsByTagName('pre')[0],
				str = '', data ,strbak ='';
			if (pre) {
				str = pre.innerHTML;
			} else {
				str = doc.body.innerHTML;
			}
			strbak = str = _unescape(str);
			iframe[0].src = 'javascript:false';
			try {
				data = K.json(str);
			} catch (e) {
				try {
					data = K.json(Url.decode(strbak));
				}catch (e2) {
					self.options.afterError.call(self, '<!doctype html><html>' + doc.body.parentNode.innerHTML + '</html>');
				}
			}
//原因：safari传来的是编码过的，转json格式时出问题，使用Url.decode转为正常字符即可。
//解决方法或许可以改请求，这段就不用改了，未研究。
</code></pre>



