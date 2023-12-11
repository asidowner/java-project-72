@import gg.jte.Content
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage

@param BasePage page = null
@param Content style = null
@param String title = "Page Analyzer"
@param Content content

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
          crossorigin="anonymous">
    <link href="${NamedRoutes.staticPath()}/main.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
            crossorigin="anonymous"></script>
    @if(style != null)${style}@endif
    <title>${title}</title>
</head>
<body class="d-flex h-100 text-center text-white bg-dark">

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <header class="mb-auto">
        <div>
            <h3 class="float-md-start mb-0">Page Analyzer</h3>
            <nav class="nav nav-masthead justify-content-center float-md-end">
                <a class="nav-link active" aria-current="page" href="${NamedRoutes.rootPath()}">Home</a>
            </nav>
        </div>
    </header>

    <main class="px-3">
        @if(page != null && page.getFlash() != null)
            <div class="alert alert-${page.getFlashType().toString()}" role="alert">
                ${page.getFlash()}
            </div>
        @endif
        ${content}
    </main>

    <footer class="mt-auto text-white-50">
        <p>created by <a href="https://github.com/asidowner" class="text-white">@asidowner</a></p>
    </footer>
</div>
</body>
</html>
