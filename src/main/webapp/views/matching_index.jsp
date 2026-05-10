<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Matching</title>

    <link rel="canonical" href="https://getbootstrap.com/docs/4.3/examples/dashboard/">

    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

    <style>
        body {
            font-size: .875rem;
        }

        .sidebar {
            position: fixed;
            top: 56px;
            bottom: 0;
            left: 0;
            z-index: 100;
            padding: 48px 0 0;
            box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
        }

        .sidebar-sticky {
            position: relative;
            top: 0;
            height: calc(100vh - 48px);
            padding-top: .5rem;
            overflow-x: hidden;
            overflow-y: auto;
        }

        .main-content {
            padding-top: 80px;
        }

        .form-section {
            max-width: 900px;
        }

        .sample-hint {
            background: #f8f9fa;
            border-left: 4px solid #007bff;
            padding: 15px;
            margin-top: 20px;
        }

        code {
            color: #333;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0"
       href="${pageContext.request.contextPath}/">
        Precision Medicine Matching System
    </a>
</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="nav.jsp"/>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4 main-content">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
                <h1 class="h2">Matching</h1>
            </div>

            <div class="form-section">
                <p class="text-muted">
                    Upload a TSV mutation file. The system will extract gene names and match them
                    with related pharmacogenomic drug labels.
                </p>

                <form method="post"
                      action="${pageContext.request.contextPath}/upload"
                      enctype="multipart/form-data">

                    <div class="form-group">
                        <label for="annovar">TSV Mutation File</label>

                        <div class="custom-file-wrapper">
                            <input type="file"
                                   id="annovar"
                                   name="annovar"
                                   accept=".tsv,text/tab-separated-values"
                                   required
                                   style="display: none;"
                                   onchange="document.getElementById('fileNameText').innerText = this.files.length > 0 ? this.files[0].name : 'No file selected';">

                            <label for="annovar" class="btn btn-outline-primary mb-0">
                                Choose File
                            </label>

                            <span id="fileNameText" class="ml-2 text-muted">
            No file selected
        </span>
                        </div>

                        <small class="form-text text-muted">
                            Required columns: Chr, Start, End, Ref, Alt, Gene.refGene.
                        </small>
                    </div>

                    <div class="form-group">
                        <label for="uploadedBy">Uploaded By</label>
                        <input type="text"
                               class="form-control"
                               id="uploadedBy"
                               value="${sessionScope.username}"
                               readonly>
                        <small class="form-text text-muted">
                            The uploader is automatically linked to your login account.
                        </small>
                    </div>

                    <button type="submit" class="btn btn-primary">Upload</button>
                </form>

                <div class="sample-hint">
                    <h5>Example TSV format</h5>
                    <pre><code>Chr	Start	End	Ref	Alt	Gene.refGene	Func.refGene	ExonicFunc.refGene	AAChange.refGene
chr10	96521657	96521657	G	A	CYP2C19	exonic	nonsynonymous SNV	CYP2C19:NM_000769
chr10	94942290	94942290	C	T	CYP2C9	exonic	nonsynonymous SNV	CYP2C9:NM_000771
chr16	31107689	31107689	G	A	VKORC1	exonic	nonsynonymous SNV	VKORC1:NM_024006</code></pre>
                </div>
            </div>
        </main>
    </div>
</div>

</body>
</html>