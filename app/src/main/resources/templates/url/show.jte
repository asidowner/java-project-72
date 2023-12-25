@import hexlet.code.dto.url.UrlPage
@import hexlet.code.util.DateTimeFormatter
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <h1 class="display-5 mb-5">Сайт: ${page.getUrl().getName()}</h1>
    <div class="table-responsive">
        <table class="table table-dark table-hover">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${DateTimeFormatter.format(page.getUrl().getCreatedAt())}</td>
            </tr>
            </tbody>
        </table>
    </div>

    <h1 class="display-5 mt-5 mb-3">Проверки</h1>
    <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post" class="rss-form text-body">
        <button type="submit" class="btn btn-lg btn-primary mt-2">Запустить проверку</button>
    </form>
    <div class="table-responsive">
        <table class="table table-dark table-hover">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Код ответа</th>
                <th scope="col">title</th>
                <th scope="col">h1</th>
                <th scope="col">description</th>
                <th scope="col">Дата проверки</th>
            </tr>
            </thead>
            <tbody>
            @for(var item : page.getUrlChecks())
                <tr>
                    <td>${item.getId()}</td>
                    <td>${item.getStatusCode()}</td>
                    <td>${item.getTitle()}</td>
                    <td>${item.getH1()}</td>
                    <td>${item.getDescription()}</td>
                    <td>${DateTimeFormatter.format(item.getCreatedAt())}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>`

)