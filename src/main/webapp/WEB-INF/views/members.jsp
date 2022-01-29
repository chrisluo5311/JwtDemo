<%--
  Created by IntelliJ IDEA.
  User: chris
  Date: 2021/10/19
  Time: 下午 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>{ ㄏMembers; }</title>
    <!-- 开发环境版本，包含了有帮助的命令行警告 -->
<%--    <script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>--%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</head>
<style>

</style>
<body>

<section class="vh-100" style="background-color: #333">
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card shadow-2-strong" style="border-radius: 1rem;">
                    <div class="card-body p-5 text-center" >

                        <h3 class="mb-5" style="font-weight: bold">Welcome</h3>

                        <div class="form-outline mb-4">
                            <input type="email" id="typeEmailX-2" class="form-control form-control-lg" />
                            <label class="form-label" for="typeEmailX-2" style="font-weight: bold;font-size: large">Email</label>
                        </div>

                        <div class="form-outline mb-4">
                            <input type="password" id="typePasswordX-2" class="form-control form-control-lg" />
                            <label class="form-label" for="typePasswordX-2" style="font-weight: bold;font-size: large">Password</label>
                        </div>

                        <!-- Checkbox -->
                        <div class="form-check d-flex justify-content-start mb-4">
                            <input
                                    class="form-check-input"
                                    type="checkbox"
                                    value=""
                                    id="form1Example3"
                            />
                            <label class="form-check-label" for="form1Example3"> Remember password </label>
                        </div>

                        <button class="btn btn-primary btn-lg btn-block" type="submit">Login</button>

                        <hr class="my-4">

                        <div class="d-grid gap-2">
                        <button class="btn btn-lg btn-block btn-primary" style="background-color: #dd4b39;" type="submit"><i class="fab fa-google me-2"></i> Sign in with google  </button>
                        <button class="btn btn-lg btn-block btn-primary mb-2" style="background-color: #3b5998;" type="submit"><i class="fab fa-facebook-f me-2"></i>Sign in with facebook</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


<script>

</script>

</body>
</html>
