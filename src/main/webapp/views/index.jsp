<%--
  Created by IntelliJ IDEA.
  User: hello
  Date: 2019-12-3
  Time: 15:37
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Dashboard</title>

    <link href="<%= request.getContextPath() %>/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="<%= request.getContextPath() %>/static/jquery/jquery-3.4.1.js"></script>
    <script src="<%= request.getContextPath() %>/static/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link href="<%= request.getContextPath() %>/static/css/app.css" rel="stylesheet">

    <style>
        .dashboard-hero {
            background: linear-gradient(135deg, #f8fbff 0%, #eef5ff 100%);
            border: 1px solid #e2e8f0;
            border-radius: 14px;
            padding: 34px 38px;
            margin-bottom: 28px;
            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.06);
        }

        .dashboard-hero h1 {
            font-size: 38px;
            font-weight: 700;
            margin-bottom: 16px;
            color: #1f2937;
        }

        .dashboard-hero p {
            font-size: 17px;
            line-height: 1.7;
            color: #4b5563;
            max-width: 980px;
            margin-bottom: 0;
        }

        .feature-card {
            height: 100%;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            background: #ffffff;
            padding: 24px;
            box-shadow: 0 6px 18px rgba(15, 23, 42, 0.05);
            transition: transform 0.15s ease, box-shadow 0.15s ease;
        }

        .feature-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 26px rgba(15, 23, 42, 0.09);
        }

        .feature-card h5 {
            font-weight: 700;
            margin-bottom: 12px;
            color: #111827;
        }

        .feature-card p {
            color: #6b7280;
            line-height: 1.6;
            margin-bottom: 18px;
        }

        .feature-card .btn {
            border-radius: 8px;
            padding-left: 18px;
            padding-right: 18px;
        }

        .section-title {
            margin-top: 8px;
            margin-bottom: 18px;
            font-size: 24px;
            font-weight: 700;
            color: #1f2937;
        }

        .workflow-box {
            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 24px 28px;
            margin-top: 28px;
            box-shadow: 0 6px 18px rgba(15, 23, 42, 0.04);
        }

        .workflow-step {
            display: flex;
            align-items: flex-start;
            margin-bottom: 16px;
        }

        .workflow-step:last-child {
            margin-bottom: 0;
        }

        .step-number {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background: #007bff;
            color: #ffffff;
            text-align: center;
            line-height: 30px;
            font-weight: 700;
            margin-right: 14px;
            flex-shrink: 0;
        }

        .step-content strong {
            color: #111827;
        }

        .step-content span {
            display: block;
            color: #6b7280;
            margin-top: 3px;
            line-height: 1.5;
        }

        .badge-soft {
            display: inline-block;
            background: #eaf2ff;
            color: #0056b3;
            border-radius: 999px;
            padding: 6px 12px;
            font-size: 13px;
            font-weight: 600;
            margin-right: 8px;
            margin-bottom: 8px;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="<%= request.getContextPath() %>/">
        Precision Medicine Matching System
    </a>
</nav>

<div class="container-fluid">
    <div class="row">

        <jsp:include page="nav.jsp">
            <jsp:param name="active" value="dashboard"/>
        </jsp:include>

        <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h2>Dashboard</h2>
            </div>

            <div class="dashboard-hero">
                <h1>Welcome to the Precision Medicine Matching System</h1>
                <p>
                    This web application supports mutation-based precision medicine exploration.
                    Users can upload annotated variant files, match detected genes with pharmacogenomic drug labels,
                    browse the PharmGKB-derived knowledge base, search clinical drug information, and save important
                    drugs to a personal Favorite list for later review.
                </p>

                <div class="mt-4">
                    <span class="badge-soft">Mutation–Drug Matching</span>
                    <span class="badge-soft">Drug Knowledge Base</span>
                    <span class="badge-soft">Extended Drug Labels</span>
                    <span class="badge-soft">Favorite Drugs</span>
                </div>
            </div>

            <h3 class="section-title">Main Functions</h3>

            <div class="row">
                <div class="col-md-6 col-xl-3 mb-4">
                    <div class="feature-card">
                        <h5>Mutation Matching</h5>
                        <p>
                            Upload ANNOVAR-style mutation files and identify drug labels that may be related to
                            detected genes or variants.
                        </p>
                        <a class="btn btn-primary btn-sm" href="<%= request.getContextPath() %>/matchingIndex">
                            Start Matching
                        </a>
                    </div>
                </div>

                <div class="col-md-6 col-xl-3 mb-4">
                    <div class="feature-card">
                        <h5>Drug Database</h5>
                        <p>
                            Browse drug records, search by drug name or ID, and check whether a drug has biomarker
                            information.
                        </p>
                        <a class="btn btn-primary btn-sm" href="<%= request.getContextPath() %>/drugs">
                            View Drugs
                        </a>
                    </div>
                </div>

                <div class="col-md-6 col-xl-3 mb-4">
                    <div class="feature-card">
                        <h5>Drug Labels</h5>
                        <p>
                            Explore FDA-derived drug label summaries with extended clinical fields such as efficacy
                            summary and response warning.
                        </p>
                        <a class="btn btn-primary btn-sm" href="<%= request.getContextPath() %>/drugLabels">
                            View Labels
                        </a>
                    </div>
                </div>

                <div class="col-md-6 col-xl-3 mb-4">
                    <div class="feature-card">
                        <h5>Favorite Drugs</h5>
                        <p>
                            Save useful drugs during browsing and revisit them later through a personal Favorite page.
                        </p>
                        <a class="btn btn-primary btn-sm" href="<%= request.getContextPath() %>/favorites">
                            View Favorites
                        </a>
                    </div>
                </div>
            </div>

            <div class="workflow-box">
                <h3 class="section-title">Suggested Workflow</h3>

                <div class="workflow-step">
                    <div class="step-number">1</div>
                    <div class="step-content">
                        <strong>Upload mutation data</strong>
                        <span>
                            Start from the Matching page and upload an annotated mutation file for a patient sample.
                        </span>
                    </div>
                </div>

                <div class="workflow-step">
                    <div class="step-number">2</div>
                    <div class="step-content">
                        <strong>Generate matched drug labels</strong>
                        <span>
                            The system extracts gene information and links it to drug-label summaries in the knowledge base.
                        </span>
                    </div>
                </div>

                <div class="workflow-step">
                    <div class="step-number">3</div>
                    <div class="step-content">
                        <strong>Review knowledge-base evidence</strong>
                        <span>
                            Search drugs, labels, and dosing guidelines to further interpret the matched results.
                        </span>
                    </div>
                </div>

                <div class="workflow-step">
                    <div class="step-number">4</div>
                    <div class="step-content">
                        <strong>Save important drugs</strong>
                        <span>
                            Add relevant drugs to Favorite for later comparison, discussion, or report preparation.
                        </span>
                    </div>
                </div>
            </div>

        </main>
    </div>
</div>

</body>
</html>