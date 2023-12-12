@import hexlet.code.dto.url.UrlPage
@import hexlet.code.util.DateTimeFormatter
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
    </div>`

)