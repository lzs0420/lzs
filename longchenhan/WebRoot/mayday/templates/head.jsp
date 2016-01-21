<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>你也知道龙宸汉吗？</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="/mayday/css/base.css">
	<script type="text/javascript" src="/mayday/js/jquery/jquery-1.8.3.js"></script>
	
  </head>
  <body>
  <div class="news-nav-box">
  <div class="news-nav">
    <div class="news-nav-list">
      <ul>
        <li class="main-lay-nav"><a href="http://www.laoyuegou.com" title="" class="news-nav-tag active">首页</a></li>
        <li class="main-lay-nav"><a href="http://lol.laoyuegou.com/league/index.html" title="" class="news-nav-tag ">英雄联盟</a></li>
        <li class="main-lay-nav"><a href="http://wow.laoyuegou.com/guild/raidLadder.html" title="" class="news-nav-tag ">魔兽排名</a> </li>
        <li class="main-lay-nav"><a href="/dota2n/leagueLT.html" title="" class="news-nav-tag ">DOTA2</a></li>
        <li class="main-lay-nav"><a href="/media/live/online/sec/online/cate/all.html" title="" class="news-nav-tag ">直播&视频</a></li>
        <li class="main-lay-nav has-sub"><a href="http://wow.laoyuegou.com/main/plus.html" title="" class="news-nav-tag ">魔兽插件</a></li>
                <li class="main-lay-nav"><a href="/site/downApp.html" title="" class="news-nav-tag " style="
    background-color: #299d88;
    color: white;
    padding-left: 10px;
    padding-right: 10px;
">App</a></li>
      </ul>
    </div>
    <!--登录-->
    <div class="news-function">
      <div class="news-login"><a id="legendary-login" href="javascript:void(0)" title="" class="login-txt">使用 App 登录</a></div>
          </div>
    <!--登录  end-->
  </div>

  <script>


    $(function() {

      $('#legendary-login').click(function() {
        popAppLayer();
      })
        $('#to-app').click(function() {
            popAppLayer2();
        })

      $('body').delegate('.lay-guid-body, .lay-guid-box, .lay-guid-box2, .lay-guid-box3', 'click', function() {
        $('.lay-guid-body, .lay-guid-box, .lay-guid-box2, .lay-guid-box3').hide();
      })

      $('.news-nav-list .main-lay-nav').hover(function() {
        $(this).find('.lay-nav').show();
      }, function() {
        $(this).find('.lay-nav').hide();
      });
    })

  </script>

</div>
<div class="header-news-box">
  <div class="header-news">
    <h1></h1>
    <span class="hews-icon"></span>
    <!--搜索-->
    <div class="header-search">
      <div class="search-tit">
        <a href="javascript:void(0)" title="" game="lol" class="tit" style="color: #209e89">英雄联盟</a>
        <a href="javascript:void(0)" title="" game="dota2" class="tit">DOTA2</a>
        <a href="javascript:void(0)" title="" game="wow" class="tit">魔兽世界</a>
      </div>
      <form action="/enter/search/search.html" id="form_search" method="get">
            <input type="hidden" id="game_type" name="type" value="lol">

            <div class="search-input-box">
              <input name="name" type="text" class="search-input" value="" placeholder="输入您的召唤师昵称  （支持全球数据）">
              <a href="javascript:void(0)" title="" onclick="document.getElementById('form_search').submit();" class="search-btn"></a>
              <input type="submit" style="display:none;">
            </div>
      </form>
                </div>
    <!--搜索 end-->
  </div>
</div>

  </body>
</html>
