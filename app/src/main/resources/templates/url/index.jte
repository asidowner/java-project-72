@import hexlet.code.util.DateTimeFormatter
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.url.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <h1 class="display-5 mb-5">Сайты</h1>
    <div class="table-responsive">
        <table class="table table-dark table-hover">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Имя</th>
                <th scope="col">Последняя проверка</th>
                <th scope="col">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(var item : page.getUrls())
                <tr>
                    <td>${item.getId()}</td>
                    <td><a href="${NamedRoutes.urlPath(item.getId())}">${item.getName()}</a></td>
                    @if(item.getLastCheckDate() != null)
                        <td>${DateTimeFormatter.format(item.getLastCheckDate())}</td>
                    @else
                        <td></td>
                    @endif
                    @if(item.getStatus() != 0)
                        <td>${item.getStatus()}</td>
                    @else
                        <td></td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    </div>`

)