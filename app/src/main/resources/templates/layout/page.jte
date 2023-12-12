@import gg.jte.Content
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage

@param BasePage page = null
@param Content content

<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link href="${NamedRoutes.staticPath()}/main.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <title>Анализатор страниц</title>
</head>
<body class="d-flex h-100 text-center text-white bg-dark">

<div class="d-flex w-100 h-100 p-3 mx-auto flex-column">
    <header class="mb-auto">
        <div>
            <h3 class="float-md-start mb-0">Анализатор страниц</h3>
            <nav class="nav nav-masthead justify-content-center float-md-end">
                <a class="nav-link" aria-current="page" href="${NamedRoutes.rootPath()}">Главная</a>
                <a class="nav-link" aria-current="page" href="${NamedRoutes.urlsPath()}">Сайты</a>
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
