<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
  <meta name="generator" content="Jekyll v3.8.5">

  <title>商品列表 ｜ NCU MIS SA</title>

  <!-- Bootstrap core CSS -->
  <link href="statics/css/bootstrap.min.css" rel="stylesheet">
  <link href="statics/icon/favicon.ico" type="image/x-icon" rel="icon">
  <link href="statics/icon/favicon.ico" type="image/x-icon" rel="shortcut icon">
  

  <style>
    .bd-placeholder-img {
      font-size: 1.125rem;
      text-anchor: middle;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
    }

    @media (min-width: 768px) {
      .bd-placeholder-img-lg {
        font-size: 3.5rem;
      }
    }
  </style>
  <!-- Custom styles for this template -->
  <link href="statics/css/product.css" rel="stylesheet">
  <link rel="stylesheet" href="../statics/css/navbar.css">

  <script src="statics/js/jquery-3.4.1.min.js"></script>
</head>

<body bgcolor="#BCE2FF">
  <div>
    <nav>
    <ul>
        <li><img src="../statics/img/icon.png" style="float: left" height="100px" width="100px" ></img></li>
        <li><a class="navbarItem" href="home.html">商品總覽</a></li>
        <li style="float:right"><a class="navbarItemSide" href="register.html">會員登入/註冊</a></li>
    </ul>
    <h5 class="my-0 mr-md-auto font-weight-normal">NCU_MIS-SA</h5>
    
    </nav>
    <a class="btn btn-outline-primary" href="cart.html">購物車</a>
  </div>

  <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
    <h1 class="display-4">商品列表</h1>
    <p class="lead">以下是所有商品包含其ID、品名、價格與說明</p>
  </div>

  <div class="album py-5 bg-light">
    <div class="container">
      <div id="product_panel" class="row">
      </div>
    </div>
  </div>

  <script>   
    var [client_cart_obj, client_cart_amount]  = getCartDataFromClient();
    
    function getAllProduct() {
      $.ajax({
        type: "GET",
        url: "api/product.do",
        crossDomain: true,
        cache: false,
        dataType: 'json',
        timeout: 5000,
        success: function (response) {
          if (response.status == 200) {
        	var product_panel = '';
        	
        	$.each(response.response.data, function (){
        		product_panel += addProduct(this);
        	})
        	
        	$("#product_panel").append(product_panel);
        	setButtonFunction();
        	checkIfExistInCart();
          }
        },
        error: function () {
          alert("無法連線到伺服器！");
        }
      });
    }

    getAllProduct();
    
    function addProduct(data) {
    	let inner_html = '';
    	inner_html += '<div class="col-md-4">';
   		inner_html += '<div class="card-header">';
   		inner_html += '<h4 class="font-weight-normal text-center text-truncate">' + data.name + '</h4>';
 		inner_html += '</div>';
 		inner_html += '<div class="card mb-4 shadow-sm">';
 		inner_html += '<img src="statics/img/product/' + data.image + '" width="100%">';
		inner_html += '<div class="card-body">';
		inner_html += '<h1 class="card-title pricing-card-title">$' + data.price + ' <small class="text-muted">/ USD</small></h1>';
		inner_html += '<p class="card-text">' + data.describe + '</p>';
		inner_html += '<div class="d-flex justify-content-between align-items-center">';
		inner_html += '<div class="btn-group">';
		inner_html += '<button id="add_cart_' + data.id + '" name="add_cart" type="button" class="btn btn-lg btn-block btn-outline-primary">加入購物車</button>';
		inner_html += '</div><small class="text-muted">id: ' + data.id + '</small>';
		inner_html += '</div></div></div></div>';
    	
		return inner_html;
    }
    
    function setButtonFunction() {
    	$('button[name="add_cart"]').click(function () {
    		var action = (this.id).split('_')[0];
    		var destination = (this.id).split('_')[1];
    		var id = (this.id).split('_')[2];
    		addProductToCart(id, 1);
        });
    }
    
    function getCartDataFromClient() {
    	let cart = JSON.parse(localStorage.getItem("client_cart_obj"));
    	let amount = JSON.parse(localStorage.getItem("client_cart_amount"));
    	cart = !cart ? new Array() : cart;
    	amount = !amount ? new Array() : amount;
    	return [cart, amount];
    }
    
    function addProductToCart(id, amount) {
    	if (!(client_cart_obj.includes(id))) {
    		client_cart_obj.push(id);
    		client_cart_amount.push(amount);
    		updateCartDataToClent();
    	}
    	
    	checkIfExistInCart();
    }
    
    function updateCartDataToClent() {
    	localStorage.setItem("client_cart_obj", JSON.stringify(client_cart_obj));
    	localStorage.setItem("client_cart_amount", JSON.stringify(client_cart_amount));
    }
    
    function checkIfExistInCart() {
    	$('button[name="add_cart"]').each(function (){
    		var action = (this.id).split('_')[0];
    		var destination = (this.id).split('_')[1];
    		var id = (this.id).split('_')[2];
    		if (client_cart_obj.includes(id)) {
    			setButtonState(this.id, false);
    		}
    		else {
    			setButtonState(this.id, true);
    		}
    		
        });
    }
    
    function setButtonState(id, action) {
    	if (!action) {
    	    $('#' + id).prop('disabled', true);
    	    $('#' + id).addClass('disabled');
    	    $('#' + id).html('已加入');
    	}
    	else {
    		$('#' + id).prop('disabled', false);
    	    $('#' + id).removeClass('disabled');
    	    $('#' + id).html('加入購物車');
    	}
    }
  </script>

  <footer class="text-muted">
    <div class="container">
      <p class="float-right">
        <a href="#">Back to top</a>
      </p>
      <p>Album example is &copy; Bootstrap, but please download and customize it for yourself!</p>
      <p>New to Bootstrap? <a href="https://getbootstrap.com/">Visit the homepage</a> or read our <a
          href="/docs/4.3/getting-started/introduction/">getting started guide</a>.</p>
    </div>
  </footer>
</body>

</html>
