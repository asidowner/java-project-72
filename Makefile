REDIRECT := make -C ./app


test:
	@$(REDIRECT) test

checkStyle:
	@$(REDIRECT) checkStyle

report:
	@$(REDIRECT) report