@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage

@param BasePage page = null

@template.layout.page(
page = page,
content = @`
    <section>
        <div class="container-fluid bg-dark p-5">
            <div class="row">
                <div class="col-md-10 col-lg-8 mx-auto text-white">
                    <h1 class="display-5 mb-0">Анализатор страниц</h1>
                    <p class="lead">Бесплатно проверяйте сайты на SEO пригодность</p>
                    <form action="${NamedRoutes.urlsPath()}" method="post" class="rss-form text-body">
                        <div class="form-floating">
                            <input id="url-input" autofocus="" type="text" required name="url" aria-label="url"
                                   class="form-control" placeholder="ссылка" autocomplete="off">
                            <label for="url-input">Ссылка</label>
                        </div>
                        <button type="submit" class="btn btn-lg btn-primary mt-2">Проверить</button>
                    </form>
                </div>
            </div>
        </div>
    </section>
`
)
